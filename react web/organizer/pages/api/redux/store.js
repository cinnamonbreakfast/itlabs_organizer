import {applyMiddleware, createStore } from 'redux'
import counter, {increment, decrement} from './action'
import reducer from './reducers'
import { persistStore, persistReducer } from 'redux-persist'
import storage from 'redux-persist/lib/storage'

const persistConfig = {
    key: 'root',
    storage,                
}
   
const persistedReducer = persistReducer(persistConfig, reducer)

let store = createStore(persistedReducer);

store.subscribe(() => { console.log(store.getState())})

store.dispatch(increment())

export const persisted = persistStore(store)

export default store