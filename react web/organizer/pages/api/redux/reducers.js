import { combineReducers } from 'redux'
import userActionReducer from './userActions'
import counter from './action'

export default combineReducers({
    counter,
    user: userActionReducer,
})