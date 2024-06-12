"use client";
import { combineReducers, configureStore } from "@reduxjs/toolkit";
// import counterReducer from "./modules/counterSlice";
import { Provider } from "react-redux";

const rootReducer = combineReducers({
//   counter: {},
  //add all your reducers here
});

export const store = configureStore({
  reducer: rootReducer,
});

export function ReduxProvider({ children }: React.PropsWithChildren) {
  return <Provider store={store}>{children}</Provider>;
}

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;

