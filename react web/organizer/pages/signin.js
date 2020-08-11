import React, { useState, useEffect } from 'react'
import styles from '../styles/pages/auth.module.scss'
import UserController from './api/userController'
import { actions } from './api/redux/userActions'
import { useSelector, useDispatch } from 'react-redux'

const SignInPage = () => {
    const userController = new UserController();
    const dispatch = useDispatch()

    const [authEmail, setAuthEmail] = useState('')
    const [authPassword, setAuthPassword] = useState('')

    const handleInputData = (event) => {
        let target = event.target;
        let identifier = target.name;

        switch(identifier) {
            case 'email':
                setAuthEmail(target.value)
                break;
            case 'password':
                setAuthPassword(target.value)
                break;
            default:
                return
        }
    }

    const caller = (e) => {
        e.preventDefault()

        userController.signInWithEmail(
            {
                email: authEmail,
                password: authPassword, 
            }
        ).then(res => {
            if(res) {
                console.log("Logged in", res.data)
                
                dispatch({type: actions.SET_AUTH_STATUS, payload: true})
                dispatch({type: actions.SET_USER_DATA, payload: res.data})
            } else {
                console.log("False info")
            }
        }).catch(err => {
            console.log(err)
        })
    }

    return (
        <div className={styles.wrapper}>
            <div className={styles.greetings}>
                <h1>Sign In</h1>
                <p>Welcome back! We're glad you returned.</p>
            </div>

            <form onSubmit={(event) => caller(event)}>
                <div className={styles.formGroup}>
                    <input type="text" name="email" onChange={(e) => handleInputData(e)} placeholder="E-Mail"/>
                </div>

                <div className={styles.formGroup}>
                    <input type="password" name="password" onChange={(e) => handleInputData(e)} placeholder="Password"/>
                </div>

                <div className={styles.formGroup}>
                    <input type="submit" name="submit" value="Sign In"/>
                    <a href="#">Reset password</a>
                </div>
            </form>
        </div>
    )
}

export default SignInPage