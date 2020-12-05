import { useState, useEffect } from 'react'
import styles from '../../styles/pages/companiesCreate.module.scss'
import { useSelector, useStore } from 'react-redux'
import axios from 'axios'

const FileUpload = (props) => {
    const [photo, setPhoto] = useState(props.photo || null)
    const [fileInput, setFileinput] = useState(null)
    const [fileName, setFileName] = useState(null)

    const displaySelected = (e) => {
        let fr = new FileReader();
        fr.onload = () => {
            setPhoto(fr.result)
            
        }
        props.update(e.target.files[0])
        setPhoto(fr.readAsDataURL(e.target.files[0]))
        setFileName(e.target.files[0].name)
    }
    
    return (
        <div className={styles.avatarSelect}>
            <div className={styles.companyLogo}>
                {photo && <img src={`${photo}`}/>}
            </div>
            <input ref={input => setFileinput(input)} type="file" id="avatar" name="avatar" accept="image/png, image/jpeg" onChange={event => displaySelected(event)}
        
            />
        </div>
    )
}


const CreateCompany = (props) => {
    const user = useSelector(state => (state.user))
    const [mode, setMode] = useState('')
    const [city, setCity] = useState('')
    const [address, setAddress] = useState('')
    const [country, setCountry] = useState('')
    const [companyCode, setCompanyCode] = useState('')
    const [category, setCategory] = useState('')
    const [companyName, setCompanyName] = useState('')
    const [searchResult, setSearchResult] = useState(null);
    const [searchCountryResult, setCountrySearchResult] = useState('');
    const [searchCityResult, setCityResult]= useState('')
    const [photo, setParentPhoto] = useState(new Blob())
    const [CUI,setCUI]=useState(null)

    const [_cityAutoSuggest, _setCityAutoSuggest] = useState(false)
    const [_countryAutoSuggest, _setCountryAutoSuggest] = useState(false)
    const [_categoryAutoSuggest, _setCategoryAutoSuggest] = useState(false)

    const [formMessage, setFormMessage] = useState(null)
    const [messageTimer, setMessageTimer] = useState(null)


    const handleInput = (event) => {
        let target = event.target
        let identifier = target.name
        let value = target.value
        if(!value) value = ''

        switch(identifier) {
            case 'company_name': setCompanyName(value); break
            case 'company_city': setCity(value); searchCountryListRequest(country, city, 'city'); _setCityAutoSuggest(true); break
            case 'company_address': setAddress(value); break
            case 'company_country': setCountry(value); searchCountryListRequest(country, city, 'country'); _setCountryAutoSuggest(true); break
            case 'company_category': setCategory(value); searchAnimeRequest(value); _setCategoryAutoSuggest(true); break
            case 'company_code': setCompanyCode(value); break
            case 'company_cui':setCUI(value); break
            default: return
        }
    }

    const handleMode = (mode) => {
        // Join or Create
        setMode(mode)
    }

    const searchAnimeRequest = (name) => {
        const url = process.env.REQ_HOST + '/fetch/animelist'

        axios.get(url, {params: { 'page': 0, 'name': name } })
        .then(_resp => {
            console.log(_resp)
            setSearchResult(_resp.data)
        })
        .catch(_err => {
            console.log(_err)
            return false
        })
        
    }

    const searchCountryListRequest = (country, city, resource) => {
        const url = process.env.REQ_HOST+'/fetch/'+ resource + 'list'

        axios.get(url, {params:{'page' : 0, 'country' : country, 'city' : city}})
        .then(_resp=> {
            if(_resp.data) {
                setCityResult(_resp.data)
            }
        })
        .catch(_err => {
            console.log(_err)
            console.log(_err.response.data)
        })
    }

    const setTextHandler = (identifier, value) =>{
        console.log(identifier, value)
        
        switch(identifier) {
            case 'city':
                setCity(value)
                break;
            case 'country':
                setCountry(value)
                break
            case 'category':
                setCategory(value)
                break
            default:
                return
        }
        
        // while(parent.tagName.toString()!='LI')
        // {
        //     parent = child.parentElement
        //     child=parent;
        // }
        // let firstChild = parent.firstElementChild
        
        // if(firstChild.className=="category_p")
        //     setCategory(firstChild.textContent)
        // else if(firstChild.className=="country_p"){
        //     setCountry(firstChild.textContent)
        // }else {
        //     setCity(firstChild.textContent)
        // }
    }

    const handleSubmitButton= (e)=>{
        e.preventDefault()
        createCompanyRequest()
        
    }

    const displayMessage = (message) => {
        setFormMessage(message)
        clearTimeout(messageTimer)
        setMessageTimer(setTimeout(() => { setFormMessage(null) }, 6000))
    }

    const createCompanyRequest = () =>{
        setFormMessage(null);

        let formData = new FormData()

        formData.append('file',photo);
        formData.append('name',companyName);
        formData.append('city',city);
        formData.append('address',address);
        formData.append('category',category);
        formData.append('country',country);
        formData.append('cui',CUI);
            console.log(formData)
        const url = process.env.REQ_HOST + '/c/create'

        axios.post(url,formData,{headers:{
            'Content-Type':'multipart/form-data',
            'token':user.token
        }}).then(_resp => {

            console.log(_resp)
            displayMessage({message: _resp.data, type: 'ok'})
        }).catch(_err => {
            console.log(_err)
            if(!(_err && _err.response)) displayMessage({message: "An unknown error has occured. Try again later.", type: 'error'})
            else
            displayMessage({message: _err.response.data, type: 'error'})
        })
    }

    const bgClick = (e) => {
        _setCityAutoSuggest(false)
        _setCountryAutoSuggest(false)
        _setCategoryAutoSuggest(false)
    }


    return (
        <div className={styles.pageWrapper} onClick={(e) => bgClick(e)}>
            <div className={styles.title}>
                <h1>New company</h1>
                <p>Register a company within Appointment app.</p>
            </div>

            <form onSubmit={handleSubmitButton} className={styles.sides} autoComplete="fuckoffchrome">
                {
                    formMessage &&
                    <div className={styles.formNotice + ' ' + (formMessage.type === 'error' ? styles.error : styles.ok)}>
                        {formMessage.message}
                    </div>
                }

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
                        <input type="text" name="company_name" value={companyName} onChange={(event) => handleInput(event)} placeholder="Enter a name"/>
                    </div>

                    <div className={styles.formGroup}>
                        <label>Photo</label>
                        <FileUpload update ={setParentPhoto} />
                    </div>

                    <div className={styles.formGroup}>
                        <label>City</label>
                        <input type="text" value={city} name="company_city" autoComplete="no_auto_complete" onChange={event => handleInput(event)} onClick={e => _setCityAutoSuggest(true)} placeholder="Enter a city"/>
                            {
                                _cityAutoSuggest && <div className={styles.autoSuggest + ' ' + styles.companies}>
                                    <ul>
                                        {
                                        Array.isArray(searchCityResult) && searchCityResult.map(c =>{
                                                return(
                                                <li key={c.id} onClick={e => setTextHandler('city', c.city)}>{c.city}</li>
                                                )
                                            })
                                        }     
                                    </ul>
                                </div>
                            }
                    </div>

                    <div className={styles.formGroup}>
                        <label>Address</label>
                        <input type="text" name="company_address"  autoComplete="fuckoffchrome" value={address} onChange={(e) => handleInput(e)} placeholder="Enter an address"/>
                    </div>

                    <div className={styles.formGroup}>
                        <label>Country</label>
                        <input type="text" name="company_country" autoComplete="no_auto_complete" value={country} onChange={(e) => handleInput(e)} onClick={e => _setCountryAutoSuggest(true)} placeholder="Enter a country"/>

                        {
                            _countryAutoSuggest &&
                            <div className={styles.autoSuggest + ' ' + styles.companies}>
                                <ul>
                                    {
                                        Array.isArray(searchCityResult) && searchCityResult.map(c => {
                                            return(<li key={c.id} onClick={e => setTextHandler('country', c.country)}>{c.country}</li>)
                                        })
                                    }     
                                </ul>
                            </div>
                        }

                    </div>

                    <div className={styles.formGroup}>
                        <label>Category</label>
                        
                        <input type="text" value={category} name="company_category" autoComplete="off" placeholder="Category" onClick={e => _setCategoryAutoSuggest(true)} onChange={e => handleInput(e)}/>
                        
                        {
                            _categoryAutoSuggest &&
                            <div className={styles.autoSuggest + ' ' + styles.companies}>
                                <ul>
                                    
                                        {Array.isArray(searchResult) && searchResult.map(c => {
                                            return(
                                                <li key={c.id} onClick={e => setTextHandler('category', c.list)}>{c.list}</li>
                                            )
                                    
                                        })
                                    }     
                                </ul>
                            </div>
                        }
                    </div>

                    <div className={styles.formGroup}>
                        <label>CUI</label>
                        <input type="text" value={CUI} name="company_cui" autoComplete="off" placeholder="CUI" onClick={e => _setCategoryAutoSuggest(true)} onChange={e => handleInput(e)}/>

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
                            <input type="text" name="company_code" value={companyCode} onChange={(e) => handleInput(e)} placeholder="Enter a code"/>
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
export { FileUpload }