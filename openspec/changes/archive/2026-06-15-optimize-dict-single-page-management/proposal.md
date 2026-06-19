## Why

The current data dictionary page already contains dictionary type and dictionary item tables, but the management flow is still visually heavy and easy to lose context when switching between type-level and item-level operations. Optimizing it into a clearer single-page master-detail workspace will let administrators maintain dictionary types and their items together with fewer jumps and fewer accidental operations.

## What Changes

- Refine `ui/src/pages/Sys/Base/dict.tsx` into a single-page dictionary workspace where the left area manages dictionary types and the right area manages items for the selected dictionary type.
- Keep the existing drawer-based create, view, and edit flows for dictionary types and dictionary items, while improving selection state, empty state, disabled states, and toolbar context.
- Preserve existing import, export, delete, pagination, search, and permission-gated operations for both dictionary types and dictionary items.
- Make dictionary item operations explicitly depend on the currently selected dictionary type, including item create/import/export/query behavior.
- Improve responsive layout so the page remains usable on wide desktop screens and stacks predictably on smaller screens.
- No API, database, or permission model changes are intended.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `data-dictionary`: Clarify the front-end requirement for managing dictionary types and dictionary items in one coordinated page with selection-driven item management.

## Impact

- Affected frontend code:
  - `ui/src/pages/Sys/Base/dict.tsx`
  - Existing drawer components may be reused as-is unless minor prop or context improvements are needed:
    - `ui/src/pages/Sys/Base/DictDrawer.tsx`
    - `ui/src/pages/Sys/Base/DictItemDrawer.tsx`
- Affected services:
  - Reuses existing `ui/src/services/admin/dict.ts`
  - Reuses existing `ui/src/services/admin/dictItem.ts`
- Affected specs:
  - `openspec/specs/data-dictionary/spec.md`
- Backward compatibility:
  - Existing REST API paths, request payloads, response handling, permissions, and routes remain unchanged.
- Database migration:
  - No database schema or seed data migration is required.
- Rollback plan:
  - Revert the frontend page changes for `dict.tsx` and any related locale-only adjustments. Since APIs and database state are unchanged, rollback does not require data repair.
