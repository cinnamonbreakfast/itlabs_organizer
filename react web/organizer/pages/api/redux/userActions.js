export const actions = {
    SET_AUTH_STATUS: 'SET_AUTH_STATUS',
    SET_USER_DATA: 'SET_USER_DATA',
}

const userActionReducer = (state = {}, action) => {
    switch(action.type) {
        case actions.SET_AUTH_STATUS:
            return Object.assign({}, state, {
                userLoggedIn: action.payload
            })
        case actions.SET_USER_DATA:
            return Object.assign({}, state, {
                data: action.payload
            })
        default:
            return state
    } 
}

export default userActionReducer