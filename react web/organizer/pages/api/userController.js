import axios from 'axios'
import { actions } from './redux/userActions'

class UserController {
    constructor(dispatcher) {
        this.dispatcher = dispatcher
    }

    signInAction(credentials) {
        let data = new FormData();
        data.set('contact', credentials.contact)
        data.set('password', credentials.password)

        return axios.post(process.env.REQ_HOST + '/u/signin', data)
    }

    setLoginData(data) {
        this.dispatcher({type: actions.SET_AUTH_STATUS, payload: true})
        this.dispatcher({type: actions.SET_USER_DATA, payload: data})
        this.dispatcher({type: actions.SET_USER_AUTH_TOKEN, payload: data.token})
        this.dispatcher({type: actions.SET_USER_AUTH_TIME, payload: data.authTime})

        getSpecialistData(data.token)
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

    async acceptInvitation(id, token) {
        let data = new FormData()
        data.set('invitationId', id)
        data.set('token', token)

        return axios({
            url: process.env.REQ_HOST + '/s/acceptInvitation',
            method: 'POST',
            data: data,
            headers: {
                'token' : token
            }
        })
        .then(resp => {
            console.log(resp)
            if(resp.data) return {message: resp.data, type: 'ok'}
            return {message: 'Strange error', type: 'error'}
        })
        .catch(err => {
            console.log(err)
            console.log(err.response)
            if(err.response) return {message: err.response.data, type: 'error'}
            return {message: 'Strange error', type: 'error'}
        })
    }

    async getSpecialistData(token) {
        return axios({
            url: process.env.REQ_HOST + '/s/invitations',
            method: 'GET',
            headers: {
                'token': token
            }
        })
        .then(resp => {
            if(resp.data) {
                this.dispatcher({type: actions.SET_USER_INVITATIONS, payload: resp.data})

                return resp.data
            }
        })
        .catch(err => {
            this.dispatcher({type: actions.SET_USER_INVITATIONS, payload: null})
            console.log(err)
            console.log(err.response)
        })
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