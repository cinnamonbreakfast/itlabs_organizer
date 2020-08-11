import { useSelector, useDispatch } from 'react-redux'
import axios from 'axios'
import { actions } from './redux/userActions'

class UserController {
    constructor(dispatcher) {
        this.dispatcher = dispatcher
    }

    signInWithEmail(credentials) {
        let fdata = new FormData();
        fdata.set('email', credentials.email)
        fdata.set('password', credentials.password)

        return axios.post('http://localhost:8080/u/auth', fdata)
        .then(_response => {
            console.log(_response)
            console.log(_response.headers)
            return _response;
        }).catch(_err => {
            return false
        })
    }

    logout() {
        this.dispatcher({type: actions.SET_AUTH_STATUS, payload: false})
        this.dispatcher({type: actions.SET_USER_DATA, payload: null})

        return true;
    }
}

export default UserController;