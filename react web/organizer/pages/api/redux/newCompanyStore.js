export const actions = {
    SET_CREATION_MODE: 'SET_CREATION_MODE',
    SET_NEW_COMPANY_NAME: 'SET_NEW_COMPANY_NAME',
    SET_NEW_COMPANY_CITY: 'SET_NEW_COMPANY_CITY',
    SET_NEW_COMPANY_ADDRESS:'SET_NEW_COMPANY_ADDRESS',
    SET_NEW_COMPANY_COUNTRY:'SET_NEW_COMPANY_COUNTRY',
    SET_NEW_COMPANY_CATEGORY:'SET_NEW_COMPANY_CATEGORY',
}

const NEW_COMPANY_DEFAULT_STATE = {
    creationMode: null, // 0 = create, 1 = join
    name: null,
    city: null,
    address: null,
    country: null,
    category: null,
}

const merge =(state, payload) => (Object.assign({}, state, payload))

const newCompanyActionReducer = (state = NEW_COMPANY_DEFAULT_STATE, action) => {
    switch(action.type) {
        case action.SET_CREATION_MODE:
            return merge(state, {creationMode: action.payload})
        case action.SET_NEW_COMPANY_NAME:
            return merge(state, {name: action.payload})
        case action.SET_NEW_COMPANY_CITY:
            return merge(state, {city: action.payload})
        case action.SET_NEW_COMPANY_ADDRESS:
            return merge(state, {address: action.payload})
        case action.SET_NEW_COMPANY_COUNTRY:
            return merge(state, {country: action.payload})
        case action.SET_NEW_COMPANY_CATEGORY:
            return merge(state, {category: action.payload})
        default:
            return state
    }
}

export default newCompanyActionReducer