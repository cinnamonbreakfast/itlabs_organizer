import axios from 'axios'
import { actions } from './redux/userActions'

class UserController {
    constructor(dispatcher) {
        this.dispatcher = dispatcher
    }

    validate(code, purpose) {
        let data = new FormData();
        data.set('code', code)
        data.set('purpose', purpose)

        return axios.post(process.env.REQ_HOST + '/u/validate/', data)
    }

    signInWithEmail(credentials) {
        let fdata = new FormData();
        fdata.set('email', credentials.email)
        fdata.set('password', credentials.password)
        // return axios.post('http://31.5.22.129:8080/u/auth', fdata)
        return axios.post(process.env.REQ_HOST + '/u/auth', fdata)
        .then(_response => {
            return _response;
        }).catch(_err => {
            console.log(_err)
            return false
        })
    }

    logout() {
        this.dispatcher({type: actions.LOGOUT})
        return true;
    }
}

export default UserController;