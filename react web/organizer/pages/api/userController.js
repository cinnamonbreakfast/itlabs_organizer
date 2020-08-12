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
        return axios.post('http://localhost:8080/u/auth', fdata,{headers:{
            'TOKEN':'f',
            'AUTH_TIME':'f'
        }})
        .then(_response => {
            
            return _response;
        }).catch(_err => {
            console.log(_err);
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