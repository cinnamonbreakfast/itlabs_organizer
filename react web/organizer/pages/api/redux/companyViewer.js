export const company_view_actions = {
    SET_VIEW_COMPANY: 1,
    CLEAR_VIEW_COMPANY: 2,
    SET_VIEW_USERNAME: 3,
    SET_FIND_CODE: 4,
}

const COMPANY_VEW_DEFAULT_STATE = {
    company: null,
    username: null,
    find_code: null,
}

const companyViewActionReducer = (state = COMPANY_VEW_DEFAULT_STATE, action) => {
    switch(action.type) {
        case company_view_actions.SET_VIEW_COMPANY:
            return Object.assign({}, state, {
                company: action.payload
            })
        case company_view_actions.CLEAR_VIEW_COMPANY:
            return Object.assign({}, state, {
                company: null
            })
        case company_view_actions.SET_VIEW_USERNAME:
            return Object.assign({}, state, {
                username: action.payload,
            })
        case company_view_actions.SET_FIND_CODE:
        return Object.assign({}, state, {
            find_code: action.payload
        })
        default:
            return state
    }
}

export default companyViewActionReducer