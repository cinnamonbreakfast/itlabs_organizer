import React, { useState, useEffect } from 'react'
import styles from '../styles/pages/auth.module.scss'
import axios from 'axios'
import UserController from './api/userController'
import { useDispatch, useSelector } from 'react-redux'
import { useRouter } from 'next/router'

const SignUpValidator = (props) => {
    const uc = props.userControl

    const [code, setCode] = useState('')
    const [formMessage, setformMessage] = useState(null)
    const phone = props.phone

    const submitCode = (event) => {
        event.preventDefault()

        uc.checkCode(phone, code, "sign_up")
        .then(resp => {
            if(resp.data.code != 200) {
                setformMessage({message: resp.data.message, type: "error"})
            } else {
                props.continue(code)
            }
        })
        .catch(err => {
            console.log(err)
        })
    }

    const handleCode = (e) => {
        let cd = e.target.value
        if(cd) setCode(cd)
        else setCode('')
    }

    return (
        <form className={styles.form} onSubmit={event => submitCode(event)}>
            <div className={styles.greetings}>
                <h1>Sign Up</h1>
                <p>Please enter the code you received to your phone.</p>
            </div>

            <div className={styles.formGroup}>
                <input type="text" value={code} onChange={e => handleCode(e)} name="code" maxLength={6} placeholder="Code"/>
            </div>

            {formMessage && <p className={styles.message + ' ' + (formMessage.type === 'error' ? styles.error:styles.ok)}>{formMessage.message}</p>}

            <div className={styles.formGroup}>
                <input type="submit" value="Validate"/>
            </div>
        </form>
    )
}

const SignUpRequest = (props) => {
    const uc = props.userControl

    const [phone, setPhone] = useState('')
    const [prefixes, setPrefixes] = useState(null)
    const [formMessage, setformMessage] = useState(null)

    const handlePhone = (e) => {
        let ph = e.target.value
        if(ph) setPhone(ph)
        else setPhone('')
    }

    const askForCode = (event) => {
        event.preventDefault();

        setformMessage(null)

        uc.sendSignUpCode("+40"+phone)
        .then(res => {
            if(res.data.code !== 200) {
                setformMessage({'message':res.data.message, type: 'error'})
            } else {
                setformMessage({'message':res.data.message, type: 'ok'})
                props.continue("+40"+phone)
            }
        })
        .catch(err => {
            console.log(err)
        })
    }

    return (
        <form className={styles.form} onSubmit={event => askForCode(event)}>
            <div className={styles.greetings}>
                <h1>Sign Up</h1>
                <p>We need to verify your phone number before.</p>
            </div>

            <div className={styles.formGroup +' '+ styles.phoneBox}>
                <label>RO +40</label>
                <input type="text" value={phone} onChange={e => handlePhone(e)} name="phone" placeholder="Phone number"/>
            </div>

            {formMessage && <p className={styles.message + ' ' + (formMessage.type === 'error' ? styles.error:styles.ok)}>{formMessage.message}</p>}

            <div className={styles.formGroup}>
                <input type="submit" value="Send code"/>
            </div>
        </form>
    )
}

const SignUpMeta = (props) => {
    const uc = props.userControl
    const router = useRouter()
    const user = useSelector(state => state.user)

    const [email, setEmail] = useState('')
    const [name, setName] = useState('')
    const [city, setCity] = useState('')
    const [country, setCountry] = useState('')
    const [password, setPassword] = useState('')
    const [password2, setPassword2] = useState('')
    const phone = props.phone;
    const code = props.code

    const handleForm = (e) => {
        let value = e.target.value
        if(!value) value = ''

        switch(e.target.name) {
            case 'name':
                setName(value)
                break
            case 'email':
                setEmail(value)
                break
            case 'city':
                setCity(value)
                break
            case 'country':
                setCountry(value)
                break
            case 'password':
                setPassword(value)
                break
            case 'password2':
                setPassword2(value)
            default:
                return
        }
    }

    const [formMessage, setFormMessage] = useState(null)

    const submitForm = (event) => {
        event.preventDefault()

        if(password !== password2) {
            setFormMessage({message: "Password fields do not match.", type: "error"})
            return
        }

        let data = { name, email, phone, city, country, password, code }

        uc.signUpAction(data)
        .then(resp => {
            if(resp) {
                if(resp.data) {
                    if(resp.data.code === 200) {
                        setLoginData(resp.data.data)
                        router.push("/")
                    } else {
                        setFormMessage({message: resp.data.message, type: "error"})
                    }
                }
            }

            setFormMessage({message: "An error occured. Try again later.", type: "error"})
        })
        .catch(err => {
            setFormMessage({message: "An error occured. Try again later.", type: "error"})
        })
    }

    return (
        <form className={styles.form} onSubmit={event => submitForm(event)}>
            <div className={styles.greetings}>
                <h1>Sign Up</h1>
                <p>Get all finished. By adding your email, a validation code will be sent to it. Remember, it's only available for 15 days.</p>
            </div>

            <div className={styles.formGroup}>
                <input onChange={e => handleForm(e)} type="text" value={name} name="name" placeholder="Full name"/>
            </div>

            <div className={styles.formGroup}>
                <input onChange={e => handleForm(e)} type="text" value={email} name="email" placeholder="E-mail address"/>
            </div>

            <div className={styles.formGroup}>
                <input onChange={e => handleForm(e)} type="text" value={city} name="city" placeholder="City"/>
            </div>

            <div className={styles.formGroup}>
                <input onChange={e => handleForm(e)} type="text" value={country} name="country" placeholder="Country"/>
            </div>

            <div className={styles.formGroup}>
                <input onChange={e => handleForm(e)} type="password" value={password} name="password" placeholder="Password"/>
            </div>

            <div className={styles.formGroup}>
                <input onChange={e => handleForm(e)} type="password" value={password2} name="password2" placeholder="Password again"/>
            </div>

            {formMessage && <p className={styles.message + ' ' + (formMessage.type === 'error' ? styles.error:styles.ok)}>{formMessage.message}</p>}

            <div className={styles.formGroup}>
                <input type="submit" value="Finish"/>
            </div>
        </form>
    )
}

const SignUpPage = () => {
    const dispatch = useDispatch()
    const userController = new UserController(dispatch)
    const [message, setMessage] = useState(null)

    const [phone, setPhone] = useState('')
    const [code, setCode] = useState('')
    const [step, setStep] = useState(0)

    const toValidate = (phone) => {
        setPhone(phone)
        setStep(1)
    }

    const toCompletion = (code) => {
        setCode(code)
        setStep(2)
    }

    return (
        <div className={styles.wrapper}>
            {step === 2 && <SignUpMeta phone={phone} code={code} userControl={userController}/>}
            {step === 1 && <SignUpValidator phone={phone} userControl={userController} continue={toCompletion}/>}
            {step === 0 && <SignUpRequest userControl={userController} continue={toValidate}/>}
        </div>
    )
}

export default SignUpPage