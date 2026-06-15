## 1. Frontend Review

- [x] 1.1 Review `ui/src/pages/Sys/Base/dict.tsx` state, table columns, toolbar actions, and selection flow.
- [x] 1.2 Review `DictDrawer` and `DictItemDrawer` props to confirm existing create, view, edit, and callback behavior can be reused.
- [x] 1.3 Confirm existing locale keys and access flags cover the optimized single-page workflow.

## 2. Frontend Implementation

- [x] 2.1 Refactor `dict.tsx` state helpers so dictionary type selection, dictionary item selection, and reload behavior are explicit and easy to follow.
- [x] 2.2 Optimize the page layout into a clear master-detail workspace with dictionary types on the left and current dictionary items on the right for desktop widths.
- [x] 2.3 Improve the dictionary item empty state and toolbar context so users can see when no dictionary type is selected and which dictionary type is active.
- [x] 2.4 Ensure dictionary item query, create, import, export, and delete actions are disabled or guarded when no dictionary type is selected.
- [x] 2.5 Ensure dictionary type changes that can invalidate the selected type clear item selection and reload the affected tables.
- [x] 2.6 Preserve existing permission checks for dictionary type and dictionary item table requests, toolbar buttons, and row actions.
- [x] 2.7 Keep existing drawer forms, request IDs, import/export loading states, pagination, search filters, and service calls compatible with current APIs.

## 3. Verification

- [x] 3.1 Run the frontend type check, lint, or available validation command for the UI project.
- [x] 3.2 Manually verify the page supports selecting a dictionary type and loading scoped dictionary items.
- [x] 3.3 Manually verify item create, import, export, and delete actions are unavailable before selecting a dictionary type.
- [x] 3.4 Manually verify authorized and unauthorized operations continue to follow existing `useAccess` permission checks.
- [x] 3.5 Manually verify the page remains usable on desktop and narrow responsive widths.
