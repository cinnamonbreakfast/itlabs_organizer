import { useRouter } from 'next/router'
import { useState, useEffect } from 'react'

import styles from '../styles/pages/validate.module.scss'
import UserController from './api/userController'

const ValidationForm = (props) => {
    const purpose = props.purpose
    const uc = new UserController(null);

    const submitForm = (event) => {
        event.preventDefault()

        uc.validate(code, purpose)
        .then(_res => {
            alert(_res.data)
        })
        .catch(_err => {
            alert(_err)
        })
    }

    return (
        <form className={styles.form} onSubmit={e => submitForm(e)}>
            <div className={styles.formGroup}>
                <input type="text" maxLength="6" name="code" placeholder="Code"/>
            
                <div className={styles.dashes}>
                    <div/>
                    <div/>
                    <div/>
                    <div/>
                    <div/>
                    <div/>
                </div>
            </div>

            <input type="submit" value="Validate"/>
        </form>
    )
}

const ValidationPage = (props) => {
    const [purpose, setPurpose] = useState(null)
    const router = useRouter()

    useEffect(() => {
        setPurpose(router.query['a'])

        return () => {
            setPurpose(router.query['a'])
        }
    }, [router])

    console.log(purpose)

    return (
        <div className={styles.wrapper}>
            <div className={styles.greetings}>
                <h1>Validation</h1>
                <p>Enter the code you received through email.</p>
            </div>

            <ValidationForm/>
        </div>
    )
}

export default ValidationPage