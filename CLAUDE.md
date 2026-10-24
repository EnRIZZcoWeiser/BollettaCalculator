# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

BollettaCalculator is a personal finance manager (expenses, incomes, payment methods, subscriptions). It is a Maven multi-module project on Java 25, Spring Boot 4.1, Vaadin 25 and Lombok.

## Commands

There is no Maven wrapper, and `mvn` is not on PATH on this machine. Use a system Maven or the one bundled with IntelliJ.

```bash
mvn clean install                                   # build all modules (shared must be installed before backend/frontend resolve it)
mvn -pl backend -am spring-boot:run                 # run REST backend on :8080
mvn -pl frontend -am spring-boot:run                # run Vaadin UI on :8081 (opens browser)
```

The backend needs PostgreSQL. `application.properties` activates the `DEV` profile. The real connection settings live in `backend/src/main/resources/application-DEV.properties`, which is gitignored and points at a remote Neon database, schema `bolletta`. Do not commit that file or print its credentials. Without it, the base file falls back to the env vars `DB_URL`, `DB_USERNAME` and `DB_PASSWORD`. Hibernate uses `ddl-auto=update`, so it changes the remote schema from the entities on every startup. The project has no tests yet.

## Architecture

There are three modules, and the backend and the frontend are **two separate Spring Boot apps** that talk over HTTP/JSON:

- **shared**: the API contract both apps use. It holds the DTOs (`dto/`), request and response objects (`api/request`, `api/response`), and constant classes in `utils/`:
  - `FilterUtils`: filter-map keys
  - `StaticUtils`: category lists
  - `NameUtils` / `NotificationUtils`: UI strings
  - `StringUtils.RESPONSE_OK/KO`: response status values
- **backend** (`:8080`): REST API under `/api/entity/<entity>/{save,delete,filter}`. All three are `POST` and take the entity's `*Request` as the body.
- **frontend** (`:8081`): Vaadin Flow UI. It does not call backend Java code directly. It sends HTTP requests with `RestTemplate` to the URLs built in `frontend/utils/APIUtils`, which hardcodes `http://localhost:8080/api`.

### Generic entity pattern

Each entity (Expense, Income, PaymentMethod) follows the same set of generic base classes. To add a new entity, copy an existing one across all of these layers:

**shared**
- `XDto extends StandardDto`
- `XRequest extends StandardRequest`: one request class covers save, delete and filter. It holds the entity fields plus filter-only fields like `dateFrom` / `dateTo`.
- `XResponse extends StandardResponse<XDto>`: carries `items`, `status` (OK/KO), `error` and `errorCode`. Errors are returned in the response body rather than as HTTP status codes. Services catch exceptions and call `createErrorResponse(...)`.

**backend**
- `domain/X extends StandardEntity`: JPA entity with a `toDto()` method. Rows are per-user through a `userId` column. `security/SEManager.getId()` currently hardcodes the user ID to `1L`, as a placeholder for real auth.
- `controller/XController extends EntityController<...>`: only sets `@RequestMapping`. The endpoints are inherited.
- `service/XService implements EntityService<...>`:
  - `save` creates or updates, depending on whether `request.id` is set.
  - `retrieve` builds a filter map with `utils/filter/XFilter.createFilterMap(request)` and passes it to the repository.
- Repositories use the Spring Data custom-fragment pattern:
  - `repository/XRepository extends StandardRepository<X>, XRepositoryExt`
  - `repository/ext/XRepositoryExt extends StandardRepositoryExt<X>` declares `retrieveByFilters(Map)`.
  - `repository/impl/XRepositoryImpl` implements `retrieveByFilters(Map)` with a dynamically built HQL string and `EntityManager`.
  - The map keys come from `FilterUtils`, so the Filter class and the RepositoryImpl must use the same keys.

**frontend**
- `controller/XController`: static helpers. They turn a `Map<String,Object>` keyed by `FilterUtils` constants into an `XRequest`, then POST it to the backend.
- `ui/list/XListView extends StandardListView<XDto, XAlterDlg, XFilterDlg>`:
  - It is a `@Route` that sits inside the `DashboardView` (`AppLayout`) drawer.
  - The base class builds the toolbar (Add/Delete/Refresh/Edit/Filter) and the grid. Subclasses implement the `*Btn()` hooks and `configureColumns()`.
- `ui/dialog/alter/XAlterDlg extends StandardAlterDlg`: the create/edit form. It implements `checkData()` and `confirm()`.
- `ui/dialog/filter/XFilterDlg extends StandardFilterDlg`: collects the filter map.
- Dialogs report back to list views through a `Runnable onConfirm` callback.

### Work in progress
- `GetterController` / `GetterService` (`/api/list/...`, returning `GetterResponse`) are stubs. They are meant for lookup lists, e.g. payment methods for dropdowns.
- `ExpenseRepositoryImpl` / `IncomeRepositoryImpl` `retrieveByFilters` still return empty lists. Only PaymentMethod has a real filter query.
- `Subscription` / `SubscriptionHistory` have domain classes but no other layers yet.
