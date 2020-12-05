import React, { useState, useEffect } from 'react'
import styles from '../styles/pages/auth.module.scss'
import UserController from './api/userController'
import { useDispatch } from 'react-redux'
import { useRouter } from 'next/router'
import { useSelector } from 'react-redux'



const RecoverAsk=(props)=>{
    const dispatch = useDispatch()
    const handleInputData = (event) => {
        let target = event.target;
        let value = target.value
         setAuthContact(value)
    }

    const userController = new UserController(dispatch);

    const user = useSelector(state => (state.user))

    const [authContact, setAuthContact] = useState('')

    const caller = (e) => {
        e.preventDefault()

        userController.forgotPassAction({contact:authContact})
        .then(resp => {
            if(resp.data) {
                if(resp.data.code != 200) {
                    if(resp.data.message) setFormMessage({message: resp.data.message, type: 'error'})
                    else props.data.setFormMessage({message: 'An unknown error occured. Try again later.', type: 'error'})
                } else {
                    props.data.setState(1)
                    
                    setFormMessage({message: resp.data.message, type: 'ok'})
                }
            } else {
                props.data.setFormMessage({message: 'An unknown error occured. Try again later.', type: 'error'})
            }
        })
        .catch(err => {
            props.data.setFormMessage({message: 'An unknown error occured. Try again later.', type: 'error'})
        })
    }

return (<div className={styles.wrapper}>
    <div className={styles.greetings}>
            <h1>Reset Password</h1>
            <p>Enter a phone number</p>
    </div>

    <form className={styles.form} onSubmit={(event) => {caller(event)}}>
        <div className={styles.formGroup}>
            <input type="text" value={authContact} name="contact" onChange={(e) => handleInputData(e)} placeholder="Phone"/>
        </div>


        <div className={styles.formGroup}>
            <input type="submit" name="submit" value="Submit"/>
    
        </div>
    </form>
</div>
);

    
}

const RecoverAction= (props)=>{
    const [authCode, setAuthCode] = useState('')
    const [authPass, setAuthPass] = useState('')
    const caller = (e) => {
        e.preventDefault()

        userController.forgotPassRecoverAction({code:authCode,password:authPass})
        .then(resp => {
            if(resp.data) {
                if(resp.data.code != 200) {
                    if(resp.data.message) setFormMessage({message: resp.data.message, type: 'error'})
                    else props.data.setFormMessage({message: 'An unknown error occured. Try again later.', type: 'error'})
                } else {
                    props.data.setState(2)
                    setFormMessage({message: resp.data.message, type: 'ok'})
                }
            } else {
                props.data.setFormMessage({message: 'An unknown error occured. Try again later.', type: 'error'})
            }
        })
        .catch(err => {
            props.data.setFormMessage({message: 'An unknown error occured. Try again later.', type: 'error'})
        })
    }

    return (<div className={styles.wrapper}>
        <div className={styles.greetings}>
            <p>Enter the received code and new password</p>
        </div>
    
        <form className={styles.form} onSubmit={(event) => {caller(event)}}>
            <div className={styles.formGroup}>
                <input type="text" value={authCode} name="code" onChange={(e) => handleInputData(e)} placeholder="Received Code"/>
            </div>
            <div className={styles.formGroup}>
                <input type="text" value={authPass} name="password" onChange={(e) => handleInputData(e)} placeholder="New password"/>
            </div>

            <div className={styles.formGroup}>
                <input type="submit" name="submit" value="Submit"/>
        
            </div>
        </form>
    </div>
    );
}


const ForgotPass= () => {

    const [state,setState]=useState(0);
    const [formMessage, setFormMessage] = useState(null)

    return (
        <div>
            
            {state===0 && <RecoverAsk data = {{setState:setState,setFormMessage:setFormMessage}} />}
            {state===1&&   <RecoverAction data = {{setState:setState,setFormMessage:setFormMessage}}/> }
            
        </div>
          
        
        
    )
}

export default ForgotPass