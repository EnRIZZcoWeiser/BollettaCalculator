# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

BollettaCalculator is a personal finance manager (expenses, incomes, payment methods, net worth tracking, subscriptions). It is a Maven multi-module project on Java 25, Spring Boot 4.1, Vaadin 25.3 and Lombok.

## Workflow

- Until the first release, commit directly on `main`: no feature branches, no PRs.
- Follow the existing structure closely. New entities should be copies of existing ones, layer by layer, with the same naming, method order and error handling. Don't introduce new patterns without asking.

## Commands

There is no Maven wrapper, and `mvn` is not on PATH on this machine. Use a system Maven or the one bundled with IntelliJ. A copy also lives under `~/.m2/wrapper/dists/apache-maven-3.9.16-bin/*/apache-maven-3.9.16/bin/mvn`.

```bash
mvn clean install                                   # build all modules (shared must be installed before backend/frontend resolve it)
mvn -pl backend -am spring-boot:run                 # run REST backend on :8080
mvn -pl frontend -am spring-boot:run                # run Vaadin UI on :8081 (opens browser; pass --vaadin.launch-browser=false to skip)
```

The backend needs PostgreSQL. `application.properties` activates the `DEV` profile. The real connection settings live in `backend/src/main/resources/application-DEV.properties`, which is gitignored and points at a remote Neon database, schema `bolletta`. Do not commit that file or print its credentials. Without it, the base file falls back to the env vars `DB_URL`, `DB_USERNAME` and `DB_PASSWORD`. Hibernate uses `ddl-auto=update`, so starting the backend changes the remote schema from the entities. The project has no tests yet.

## Architecture

There are three modules, and the backend and the frontend are **two separate Spring Boot apps** that talk over HTTP/JSON:

- **shared**: the API contract both apps use. It holds the DTOs (`dto/`), request and response objects (`api/request`, `api/response`), and constant classes in `utils/`:
  - `FilterUtils`: map keys, used both for filters and for the data maps the dialogs build
  - `StaticUtils`: category lists (expense, income, net worth)
  - `NameUtils` / `NotificationUtils`: UI strings
  - `StringUtils.RESPONSE_OK/KO`: response status values
- **backend** (`:8080`):
  - CRUD API under `/api/entity/<entity>/{save,delete,filter}`. All three are `POST` and take the entity's `*Request` as the body. `filter` with an empty request returns every row of the current user.
  - Lookup lists under `/api/list/<entity>` (`GET`, `GetterController` / `GetterService`). They return the existing typed `XResponse` with all rows of the user, and feed the frontend combo boxes.
- **frontend** (`:8081`): Vaadin Flow UI. It does not call backend Java code directly. It sends HTTP requests with `RestTemplate` to the URLs built in `frontend/utils/APIUtils`, which hardcodes `http://localhost:8080/api`.

### Generic entity pattern

Each entity (Expense, Income, PaymentMethod, Asset, NetWorth, NetWorthEntry) follows the same set of generic base classes. To add a new entity, copy an existing one across all of these layers:

**shared**
- `XDto extends StandardDto`
- `XRequest extends StandardRequest`: one request class covers save, delete and filter. It holds the entity fields plus filter-only fields like `dateFrom` / `dateTo`.
- `XResponse extends StandardResponse<XDto>`: carries `items`, `status` (OK/KO), `error` and `errorCode`. Errors are returned in the response body rather than as HTTP status codes. Services catch exceptions and call `createErrorResponse(error, errorCode)`. The codes live in the backend `utils/ErrorUtils`.

**backend**
- `domain/X extends StandardEntity`: JPA entity with a `toDto()` method. Rows are per-user through a `userId` column. `security/SEManager.getId()` hardcodes the user ID to `1L`. This is intentional for now: the app has a single user and authentication comes later.
- `controller/XController extends EntityController<...>`: only sets `@RequestMapping`. The endpoints are inherited.
- `service/XService implements EntityService<...>`:
  - `save` creates or updates, depending on whether `request.id` is set.
  - `retrieve` builds a filter map with `utils/filter/XFilter.createFilterMap(request)` and passes it to the repository.
- Repositories use the Spring Data custom-fragment pattern:
  - `repository/XRepository extends StandardRepository<X>, XRepositoryExt`
  - `repository/ext/XRepositoryExt extends StandardRepositoryExt<X>`, which declares `retrieveByFilters(Map)` and `retrieveByUser(Long)`.
  - `repository/impl/XRepositoryImpl` implements both with a dynamically built HQL string and `EntityManager`.
  - The map keys come from `FilterUtils`, so the Filter class and the RepositoryImpl must use the same keys. Related entities are filtered by id (`e.paymentMethod.id = :paymentMethodId`), never by comparing an entity to a DTO.

**frontend**
- `controller/XController`: static helpers. They turn a `Map<String,Object>` keyed by `FilterUtils` constants into an `XRequest`, then POST it to the backend. `controller/GetterController` calls the `/api/list` endpoints.
- `ui/list/XListView extends StandardListView<XDto>`:
  - It is a `@Route` registered in the `DashboardView` (`AppLayout` with a `SideNav`).
  - The base class builds the icon toolbar (Add/Edit/Delete/Refresh/Filter) and the grid. Subclasses implement the `*Btn()` hooks and `configureColumns()`. `createGrid()` can be overridden to use a different grid, e.g. a `TreeGrid`.
  - Lookup lists, e.g. the payment methods, are loaded once in the list view constructor and passed to the dialogs.
- `ui/dialog/alter/XAlterDlg extends StandardAlterDlg`: the create/edit form. It implements `checkData()` and `confirm()`. `newItem` tells creation from editing.
- `ui/dialog/filter/XFilterDlg extends StandardFilterDlg`: collects the filter map.
- Dialogs report back to list views through a `Runnable onConfirm` callback.
- The theme is Aura. Use the `ButtonVariant.AURA_*` variants: `LUMO_ERROR` does nothing there, the red variant is `AURA_DANGER`.

### Net worth

This replaces a monthly spreadsheet: rows are assets, columns are dates.
- `Asset`: the master data (name, category from `StaticUtils.NET_WORTH_CATEGORIES`, active).
- `NetWorth`: one snapshot (a date). Saving a **new** snapshot creates one `NetWorthEntry` per active asset, with an empty value that the user fills in.
- `NetWorthEntry`: a single value, linked to a `NetWorth` and an `Asset`. Deleting a `NetWorth` deletes its entries first.
- Totals are never stored. `NetWorthService.calculateTotals` computes three of them:
  - `total`: sum of the entries
  - `investments`: the same sum without the `LIQUIDITY` category
  - `spending`: sum of the Expenses in the calendar month of the snapshot date
- UI: `NetWorthListView` lists the snapshots. Double-clicking a row navigates to `ui/detail/NetWorthDetailView` (`/netWorth/detail/<id>`). That view is a `StandardListView` with a `TreeGrid` grouped by category: the group rows are built client-side, have `id == null` and show subtotals. It also has `Breadcrumbs` back to the list.
- Everything is in EUR. There are no liabilities yet.

### Work in progress
- `Subscription` / `SubscriptionHistory` are empty classes with no other layers yet.
- `StandardListView` still shows a placeholder "Footer" text.
