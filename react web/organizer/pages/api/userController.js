import axios from 'axios'
import { actions } from './redux/userActions'

class UserController {
    constructor(dispatcher) {
        this.dispatcher = dispatcher
    }

    signInAction(credentials) {
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

    setLoginData(data) {
        this.dispatcher({type: actions.SET_AUTH_STATUS, payload: true})
        this.dispatcher({type: actions.SET_USER_DATA, payload: data})
        this.dispatcher({type: actions.SET_USER_AUTH_TOKEN, payload: data.token})
        this.dispatcher({type: actions.SET_USER_AUTH_TIME, payload: data.authTime})
    }

    sendSignUpCode(phone) {
        let data = new FormData()
        data.set('phone', phone)
        console.log(phone)

        return axios.post(process.env.REQ_HOST + '/u/presignup', data)
    }

    signUpAction(data) {
        return axios.post(process.env.REQ_HOST + '/u/signup', data)
    }

    checkCode(contact, code, purpose) {
        let data = new FormData()
        data.set('contact', contact)
        data.set('code', code)
        data.set('purpose', purpose)

        console.log(contact)

        return axios.post(process.env.REQ_HOST + '/u/validate', data)
    }

    logout() {
        this.dispatcher({type: actions.SET_AUTH_STATUS, payload: false})
        this.dispatcher({type: actions.SET_USER_DATA, payload: null})

        return true;
    }
}

export default UserController;