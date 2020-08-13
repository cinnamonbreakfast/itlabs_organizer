import React, { useState, useEffect } from 'react'
import styles from '../styles/pages/auth.module.scss'
import axios from 'axios'

const SignUpPage = () => {

    const [errorMsg, setMessage] = useState(null);

    const [userRole, setUserRole] = useState(0);
    const [userName, setName] = useState("")
    const [userEmail, setEmail] = useState("")
    const [userCity, setCity] = useState("")
    const [userCountry, setCountry] = useState("")
    const [userPhone, setPhone] = useState("")
    const [userPassword, setPassword] = useState("")

    const handleInputData = (event) => {
        let target = event.target;
        let identifier = target.name;
        switch(identifier) {
            case 'name':
                setName(target.value)
                break;
            case 'email':
                setEmail(target.value)
                break;
            case 'city':
                setCity(target.value)
                break;
            case 'country':
                setCountry(target.value)
                break;
            case 'phone':
                setPhone(target.value)
                break;
            case 'password':
                setPassword(target.value)
                break;
            default:
                return;
                    
        }
    }

    const formSubmit = (event) => {
        console.log("event was called");
        event.preventDefault();

        axios.post('http://31.5.22.129:8080/u/signup',
        {
            email: userEmail,
            name: userName,
            phone: userPhone,
            role: userRole,
            city: userCity,
            country: userCountry,
            password: userPassword,
            id: null
        }, {
            'Content-Type': 'application/json',
        })
        .then(response => {
            setMessage({type: "ok", message: response.data})
        })
        .catch(error => {
            setMessage({type:"error", message: error.response.data})
        })
    }

    return (
        <div className={styles.wrapper}>
            <div className={styles.greetings}>
                <h1>Sign Up</h1>
                <p>Tell us who you are. Set up your account.</p>
            </div>

            
            {
                errorMsg !== null ?
                    (
                    <p className={styles.message + ' ' + (errorMsg.type === 'error' ? styles.error : styles.ok)}>{errorMsg.message}</p>
                    )
                : (null)
            }

            {
                errorMsg === null || errorMsg.type !== "ok" ? 
                (
                    <form onSubmit={(e) => formSubmit(e)}>
                        <div className={styles.formGroup}>
                            <input type="text" name="name" onChange={(e) => handleInputData(e)} placeholder="First & Last name"/>
                        </div>

                        <div className={styles.formGroup}>
                            <input type="text" name="email" onChange={(e) => handleInputData(e)} placeholder="E-Mail"/>
                        </div>

                        <div className={styles.formGroup}>
                            <input type="text" name="city" onChange={(e) => handleInputData(e)} placeholder="City"/>
                        </div>

                        <div className={styles.formGroup}>
                            <input type="text" name="country" onChange={(e) => handleInputData(e)} placeholder="Country"/>
                        </div>

                        <div className={styles.formGroup}>
                            <input type="text" name="phone" onChange={(e) => handleInputData(e)} placeholder="Phone"/>
                        </div>

                        <div className={styles.formGroup}>
                            <input type="password" name="password" onChange={(e) => handleInputData(e)} placeholder="Password"/>
                        </div>

                        <div className={styles.formGroup + ' ' + styles.formButtons}>
                            <input type="submit" name="submit" value="Sign Up"/>
                            <input type="button" name="submit" value="Clear"/>
                        </div>
                    </form>
                ) :
                (null)
            }
            
        </div>
    )
}

export default SignUpPage