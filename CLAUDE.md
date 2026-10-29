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
  - Dashboard summary under `POST /api/dashboard/summary` (`DashboardController` / `DashboardService`), described below.
- **frontend** (`:8081`): Vaadin Flow UI. It does not call backend Java code directly. It sends HTTP requests with `RestTemplate` to the URLs built in `frontend/utils/APIUtils`, which hardcodes `http://localhost:8080/api`.

### Generic entity pattern

Each entity (Expense, Income, Transfer, PaymentMethod, Asset, NetWorth, NetWorthEntry, Subscription, SubscriptionHistory, Investment, InvestmentHistory) follows the same set of generic base classes. To add a new entity, copy an existing one across all of these layers:

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
- `ui/DashboardView` is only the app shell (`AppLayout` with a `SideNav`), used as `layout` by every route. The logo and title in the navbar are a `RouterLink` to the start page "/", which is `ui/HomeView`.
- `ui/list/XListView extends StandardListView<XDto>`:
  - It is a `@Route` with `layout = DashboardView.class`, linked from the `SideNav`.
  - The base class builds the icon toolbar (Add/Edit/Delete/Refresh/Filter), the grid (single selection) and a small footer with the author's contacts (`NameUtils.AUTHOR_*`). Subclasses implement the `*Btn()` hooks and `configureColumns()`. `createGrid()` can be overridden to use a different grid, e.g. a `TreeGrid`.
  - Lookup lists, e.g. the payment methods, are loaded once in the list view constructor and passed to the dialogs.
- `ui/detail/XDetailView`: the detail of a header entity (master-detail). It is a `StandardListView` of the child rows with `HasUrlParameter<Long>` (`/x/detail/<id>`) and `Breadcrumbs` back to the list. The list view opens it on row double-click.
- `ui/dialog/alter/XAlterDlg extends StandardAlterDlg`: the create/edit form. It implements `checkData()` and `confirm()`. `newItem` tells creation from editing. Combo boxes use `PartialMatchMode.FIRST_MATCH`.
- `ui/dialog/filter/XFilterDlg extends StandardFilterDlg`: collects the filter map.
- Dialogs report back to list views through a `Runnable onConfirm` callback.
- `utils/NotificationHelper` shows success/error notifications that close after 3 seconds.
- The theme is Aura. Use the generic `ButtonVariant.PRIMARY` / `ERROR` and `NotificationVariant.SUCCESS` / `ERROR` (Vaadin 25.3). The `LUMO_*` variants are for the Lumo theme.

### Transfers

`Transfer` records money moved between two payment methods, e.g. an ATM withdrawal (bank → cash) or a top-up. It has a date, `fromPaymentMethod`, `toPaymentMethod`, money and a note. It is neither an expense nor an income: it only moves liquidity.
- The filter has `FROM` and `TO` for one side each, and `PAYMENT_METHOD`, which matches transfers where the payment method is on either side.
- The alter dialog rejects From == To.

### Net worth

This replaces a monthly spreadsheet: rows are assets, columns are dates.
- `Asset`: the master data (name, category from `StaticUtils.NET_WORTH_CATEGORIES`, active).
- `NetWorth`: one snapshot (a date). Saving a **new** snapshot creates one `NetWorthEntry` per active asset, with an empty value that the user fills in.
- `NetWorthEntry`: a single value, linked to a `NetWorth` and an `Asset`. Deleting a `NetWorth` deletes its entries first.
- Totals are never stored. `NetWorthService.calculateTotals` computes three of them:
  - `total`: sum of the entries
  - `investments`: the same sum without the `LIQUIDITY` category
  - `spending`: sum of the Expenses in the calendar month of the snapshot date, plus the monthly amount (price / months) of the subscription periods covering that month
- UI: `NetWorthListView` lists the snapshots. Double-clicking a row opens `ui/detail/NetWorthDetailView` (`/netWorth/detail/<id>`), which uses a `TreeGrid` grouped by category: the group rows are built client-side, have `id == null` and show subtotals.
- Everything is in EUR. There are no liabilities yet.

### Subscriptions

Only subscriptions **paid in full up front** for several months (Spotify for a year, a 2-year plan…) are tracked here. Monthly ones are entered as normal Expenses, so the payment-method liquidity matches the real balance on any day of the month. A subscription must never be entered in both places.
- `Subscription`: the header (name, active). The list shows `price` and `monthlyAmount`. These are computed by `SubscriptionService.calculateCurrent` from the history row active today, or from the most recent row if none is active.
- `SubscriptionHistory`: one prepaid period with start month, end month, price and payment method. There is no type field, and the period can be any length (12, 18, 24 months…).
  - **Only the month counts.** `SubscriptionHistoryService.save` stores the start as the first day of its month and the end as the last day of its month (`SubscriptionHistory.firstDayOfMonth` / `lastDayOfMonth`).
  - `months()`: the months covered, start and end included (10/2026 − 09/2027 = 12).
  - `monthlyAmount()`: price / `months()`, counted in each of those months.
  - `isActiveOn(date)` checks the dates.
  - The `DATE_FROM` / `DATE_TO` filter selects the rows that overlap a period.
  - For cash, the full price leaves the payment method on the stored start date (the 1st of the month). The row is entered on the day it is paid, so the liquidity "to date" is still right.
- Deleting a `Subscription` deletes its history first.
- UI: `SubscriptionListView`, then a double-click opens `ui/detail/SubscriptionDetailView` (`/subscription/detail/<id>`).
  - The alter dialog has no day at all: start and end are each chosen with two combo boxes, year (`StaticUtils.YEARS`) and month (`StaticUtils.MONTHS`, shown by name), like the dashboard. All four are required. The end month is the last month covered, included. A new period starts from the current month, and the end is proposed as start + 11 months while it is empty. The helper text under the end month shows the number of months. The dialog sends the first day of the start month and the last day of the end month.
  - The grid shows the months as MM/yyyy plus a "Months" column.

### Investments

This section tracks buys and sells of ETFs, stocks and so on. They are neither expenses nor incomes, and they are completely separate from the net worth, where market values are entered by hand.
- `Investment`: the header (name, category from `StaticUtils.INVESTMENT_CATEGORIES`, active). The list shows four values computed by `InvestmentService.calculatePosition`:
  - `shares`: bought − sold;
  - `averagePrice`: buy amount / bought shares, fees excluded;
  - `invested`: shares × averagePrice;
  - `fees`: the sum of all fees.
- `InvestmentHistory`: one operation with date, type (`BUY` / `SELL`, `StaticUtils.INVESTMENT_TYPES`), amount, fee, shares, payment method and note.
  - `amount` is the money that goes into or comes out of the investment. The fee is on top: a buy takes `amount + fee` from the payment method, a sell gives back `amount − fee`.
  - `shares` is mapped with `@Column(precision = 20, scale = 8)`. A plain `BigDecimal` column keeps only 2 decimals, so any new field that needs more must declare its scale.
  - The price per share (`amount / shares`) is computed in `toDto()`, never stored.
  - `InvestmentHistoryService.save` refuses a sell with more shares than are held (`API_010`). The row being edited is excluded from the count.
- UI: `InvestmentListView`, then a double-click opens `ui/detail/InvestmentDetailView` (`/investment/detail/<id>`). A new operation defaults to `BUY` with fee 0.
- Gains are not computed. Dividends are entered by hand as Income.

### Dashboard (start page)

`ui/HomeView` (route "") shows one month, chosen with two combo boxes. Their values are hardcoded in `StaticUtils.YEARS` / `MONTHS`.
- `DashboardService.getSummary(year, month)` returns a single `DashboardDto` with:
  - `moneyIn`: the incomes of the month;
  - `moneyOut`: the expenses of the month plus `monthlyAmount()` of the subscription periods covering it. The subscriptions are grouped under `StaticUtils.SUBSCRIPTION_CATEGORY`;
  - the two lists of `CategoryAmountDto` (category, amount, percentage of the total), ordered from the biggest category.
- `liquidity`: one `LiquidityDto` per payment method, computed by `DashboardService.calculateLiquidity`. It is the balance at **today's date**, not tied to the selected month, made of all the movements since the beginning:
  - + incomes − expenses
  - + incoming transfers − outgoing transfers
  - − subscription periods (full price on the start date)
  - − buys (amount + fee) + sells (amount − fee)

  Inactive payment methods are shown only while their balance is not zero. There is deliberately no "total" card. The cards are laid out in a CSS grid with 3 slots per row (with 2 payment methods they take 2/3 of the width).
- Vaadin Charts is a paid product, so it is not used. The categories are drawn as horizontal bars made of plain `Div`s: the biggest category fills the bar and the others are scaled on it. Above them there are three cards: Money In, Money Out and Balance.

### Opening balances

The app is used from 01/01/2027. The starting situation was entered **directly in the DB** on 31/12/2026:
- one `Transfer` per payment method with an empty `from` (the opening balance);
- one `BUY` per investment with an empty payment method (the starting position: shares held and total amount spent).

An empty payment method means "outside the app": the movement is skipped on that side by the liquidity calculation, and the `toDto()` / grids handle the null. The alter dialogs still require those fields, so these rows must be edited in the DB, not in the UI. Rows inserted by hand need `user_id = 1`.

Subscriptions paid before 2027 were not entered. This is a known and accepted gap: their monthly quota is missing from the 2027 spending.

### Work in progress
- Optional subscription category (same values as expenses). When empty, the subscription is shown under a "SUBSCRIPTION" category in the summaries. Proposed but not implemented yet.
