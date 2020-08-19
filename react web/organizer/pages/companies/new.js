import { useState, useEffect } from 'react'
import styles from '../../styles/pages/companiesCreate.module.scss'
import { useSelector } from 'react-redux'

const FileUpload = () => {
    const [photo, setPhoto] = useState(null)
    const [fileInput, setFileinput] = useState(null)
    const [fileName, setFileName] = useState(null)

    const displaySelected = (e) => {
        let fr = new FileReader();
        fr.onload = () => {
            setPhoto(fr.result)
        }

        setPhoto(fr.readAsDataURL(e.target.files[0]))
        console.log(e.target.files[0].name)
        setFileName(e.target.files[0].name)
    }

    return (
        <div className={styles.avatarSelect}>
            <div className={styles.companyLogo}>
                {photo && <img src={`${photo}`}/>}
            </div>
            <input ref={input => setFileinput(input)} type="file" id="avatar" name="avatar" accept="image/png, image/jpeg" onChange={event => displaySelected(event)}/>
        </div>
    )
}

const CreateCompany = (props) => {
    const user = useSelector(state => (state.user))

    const [mode, setMode] = useState(null)
    const [city, setCity] = useState(null)
    const [address, setAddress] = useState(null)
    const [country, setCountry] = useState(null)
    const [category, setCategory] = useState(null)
    const[companyName, setCompanyName] = useState("")

    const changeCompanyName = (event) => {
        if(!event.target.value) {
            setCompanyName('')
            return false;
        }

        setCompanyName(event.target.value)
    }

    const handleMode = (mode) => {
        setMode(mode)
    }

    return (
        <div className={styles.pageWrapper}>
            <div className={styles.title}>
                <h1>New company</h1>
                <p>Register a company within Appointment app.</p>
            </div>

            <form onSubmit={(e) => e.preventDefault()} className={styles.sides}>
                <div className={styles.left_side}>
                    <div className={styles.formGroup + ' ' + styles.inLine}>
                        <div className={styles.card} onClick={() => handleMode('create')}>
                            <p><strong>Create</strong> a new company and add your employees.</p>
                            <img src="/create/new.svg"/>
                        </div>

                        <div className={styles.card} onClick={() => handleMode('join')}>
                            <p><strong>Join</strong> a company with an invite code or link.</p>
                            <img src="/create/join.svg"/>
                        </div>

                        <input type="hidden" value={mode}/>
                    </div>
                </div>

                { mode === "create" &&
                <div className={styles.right_side}>
                    <div className={styles.formGroup}>
                        <label>Company name</label>
                        <input type="text" name="company_name" value={companyName} onChange={(e) => setCompanyName(e.target.value)} placeholder="Enter a name"/>
                    </div>

                    <div className={styles.formGroup}>
                        <label>Photo</label>
                        <FileUpload/>
                    </div>

                    <div className={styles.formGroup}>
                        <label>City</label>
                        <input type="text" name="company_city" value={city} onChange={(e) => setCity(e.target.value)} placeholder="Enter a city"/>
                    </div>

                    <div className={styles.formGroup}>
                        <label>Address</label>
                        <input type="text" name="company_address" value={address} onChange={(e) => setAddress(e.target.value)} placeholder="Enter an address"/>
                    </div>

                    <div className={styles.formGroup}>
                        <label>Country</label>
                        <input type="text" name="company_country" value={country} onChange={(e) => setCountry(e.target.value)} placeholder="Enter a country"/>
                    </div>

                    <div className={styles.formGroup}>
                        <label>Category</label>
                        
                        <select name="company_category">
                            <option value="beauty">Beauty</option>
                            <option value="restaurant">Restaurant</option>
                            <option value="medicallab">Medical laboratory</option>
                            <option value="medicalclinic">Medical clinic</option>
                        </select>
                    </div>

                    <div className={styles.formGroup + ' ' + styles.inLine}>
                        <input type='submit' value='Create'/>
                        <input type='button' value='Clear'/>
                    </div>
                </div> }

                { mode === 'join' &&
                    <div className={styles.right_side}>
                        <div className={styles.formGroup}>
                            <label>Invite code</label>
                            <input type="text" name="company_name" value={companyName} onChange={(e) => setCompanyName(e.target.value)} placeholder="Enter a name"/>
                        </div>

                        <div className={styles.formGroup + ' ' + styles.inLine}>
                            <input type='submit' value='Join'/>
                            <input type='button' value='Clear'/>
                        </div>
                    </div>
                }
                
            </form>
        </div>
    )
}

export default CreateCompany