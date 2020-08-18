import { useState } from 'react'
import styles from '../styles/pages/companiesCreate.module.scss'
import { useSelector } from 'react-redux'

const Page0 = (props) => {

    return (
        <div className={styles.page + ' ' + styles.inLine + ' ' + styles.firstPage}>
            <div className={styles.card}>
                <p><strong>Create</strong> a new company and add your employees.</p>
                <img src="/create/new.svg"/>
            </div>
            <div className={styles.card}>
                <p><strong>Join</strong> a company with an invide code or link.</p>
                <img src="/create/join.svg"/>
            </div>
        </div>
    )
}

const Page1 = (props) => {
    const [photo, setPhoto] = useState(null)
    const displaySelected = (e) => {
        let fr = new FileReader();
        fr.onload = () => {
            setPhoto(fr.result)
        }

        setPhoto(fr.readAsDataURL(e.target.files[0]))
        console.log(e.target.files[0].name)
    }

    return (
        <div className={styles.page + ' ' + styles.inLine}>
            <div className={styles.formGroup}>
                <label>Company name</label>
                <input type="text" value={props.targetProps[0]} placeholder="Enter a company name" onChange={(event) => props.handler(event)}/>
            </div>

            <div className={styles.formGroup + ' ' + styles.avatarSelect}>
                <div className={styles.companyLogo}>
                    {photo && <img src={`${photo}`}/>}
                </div>
                <input type="file" id="avatar" name="avatar" accept="image/png, image/jpeg" onChange={event => displaySelected(event)}/>
            </div>
        </div>
    )
}

const CreateCompany = (props) => {
    const user = useSelector(state => (state.user))
    const [page, setPage] = useState(0)

    const[companyName, setCompanyName] = useState("")

    const changeCompanyName = (event) => {
        if(!event.target.value) {
            return false;
        }

        setCompanyName(event.target.value)
    }

    return (
        <div className={styles.pageWrapper}>
            <div className={styles.title}>
                <h1>New company</h1>
                <p>Register a company within Appointment app.</p>
            </div>

            <div className={styles.contentPages}>
                <Page0/>
                {/* <Page1 handler={changeCompanyName} targetProps={[companyName]}/> */}
            </div>

            <div className={styles.bottomControlls}>
                <button onClick={() => props.cancel()}>Back</button>
                <button>Next</button>
            </div>
        </div>
    )
}

export default CreateCompany