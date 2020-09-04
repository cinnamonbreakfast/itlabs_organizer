import { combineReducers } from 'redux'
import userActionReducer from './userActions'
import companyView from './companyViewer'

export default combineReducers({
    companyView,
    user: userActionReducer,
})