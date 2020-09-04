export const actions = {
    SET_AUTH_STATUS: 'SET_AUTH_STATUS',
    SET_USER_DATA: 'SET_USER_DATA',
    SET_USER_AUTH_TOKEN: 'SET_USER_AUTH_TOKEN',
    SET_USER_AUTH_TIME: 'SET_USER_AUTH_TIME',
    LOGOUT: 'LOGOUT',
    SET_USER_INVITATIONS: 'SET_USER_INVITATIONS',
}

const USER_DEFAULT_STATE = {
    userLoggedIn: false,
    data: null,
    token: null,
    auth_time: null,
    invitations: null,
}

const userActionReducer = (state = USER_DEFAULT_STATE, action) => {
    switch(action.type) {
        case actions.SET_AUTH_STATUS:
            return Object.assign({}, state, {
                userLoggedIn: action.payload
            })
        case actions.SET_USER_DATA:
            return Object.assign({}, state, {
                data: action.payload
            })
        case actions.SET_USER_AUTH_TOKEN:
            return Object.assign({}, state, {
                token: action.payload
            })
        case action.SET_USER_AUTH_TIME:
            return Object.assign({}, state, {
                auth_time: action.payload
            })
        case actions.LOGOUT:
            return USER_DEFAULT_STATE
        case actions.SET_USER_INVITATIONS:
            return Object.assign({}, state, {
                invitations: action.payload
            })
        default:
            return state
    } 
}

export default userActionReducer