import React, { useState, useEffect } from 'react'
import styles from '../styles/pages/auth.module.scss'
import UserController from './api/userController'
import { useDispatch } from 'react-redux'
import { useRouter } from 'next/router'
import { useSelector } from 'react-redux'

const SignInPage = () => {
    const dispatch = useDispatch()
    const userController = new UserController(dispatch);
    const router = useRouter()
    const user = useSelector(state => (state.user))

    const [authContact, setAuthContact] = useState('')
    const [authPassword, setAuthPassword] = useState('')

    const [formMessage, setFormMessage] = useState('')

    const handleInputData = (event) => {
        let target = event.target;
        let identifier = target.name;
        let value = target.value
        if(!value) value = ''

        switch(identifier) {
            case 'contact':
                setAuthContact(value)
                break;
            case 'password':
                setAuthPassword(value)
                break;
            default:
                return
        }
    }

    useEffect(() => {
        if(user.data && user.userLoggedIn) router.push("/")

    },[])

    const caller = (e) => {
        e.preventDefault()

        userController.signInAction({contact: authContact, password: authPassword})
        .then(resp => {
                console.log(resp.data)
            if(resp.data) {
                if(resp.data.code != 200) {
                    if(resp.data.message) setFormMessage({message: resp.data.message, type: 'error'})
                    else {setFormMessage({message: 'An unknown error occured. Try again later.', type: 'error'})
                }
                } else {
                    setFormMessage({message: resp.data.message, type: 'ok'})
                    userController.setLoginData(resp.data.data)
                    setTimeout( ()=>{
                        router.push("/")
                    },1000)
                }
            } else {
                setFormMessage({message: 'An unknown error occured. Try again later.(123)', type: 'error'})
            }
        })
        .catch(err => {
            console.log('fasdfasd')
            console.log(err)
            setFormMessage({message: 'An unknown error occured. Try again later.', type: 'error'})
        })
    }

    return (
        <div className={styles.wrapper}>
            <div className={styles.greetings}>
                <h1>Sign In</h1>
                <p>Welcome back! We're glad you returned.</p>
            </div>

            <form className={styles.form} onSubmit={(event) => {caller(event)}}>
                <div className={styles.formGroup}>
                    <input type="text" value={authContact} name="contact" onChange={(e) => handleInputData(e)} placeholder="E-Mail or Phone"/>
                </div>

                <div className={styles.formGroup}>
                    <input type="password" value={authPassword} name="password" onChange={(e) => handleInputData(e)} placeholder="Password"/>
                </div>

                {formMessage && <p className={styles.message + ' ' + (formMessage.type === 'error' ? styles.error:styles.ok)}>{formMessage.message}</p>}

                <div className={styles.formGroup}>
                    <input type="submit" name="submit" value="Sign In"/>
                    <a href="forgotpassword">Reset password</a>
                </div>
            </form>
            
        </div>
        
    )
}

export default SignInPage