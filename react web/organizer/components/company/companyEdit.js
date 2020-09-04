import { useState } from 'react'
import styles from '../../styles/pages/companyView.module.scss'
import axios from 'axios'
import { useSelector, useDispatch } from 'react-redux'

import { FileUpload } from '../../pages/companies/new'
import CompanyController from '../../pages/api/companyController'

const CompanyEdit = (props) => {
    const company = props.company
    const user = useSelector(state => (state.user))
    const dispatch = useDispatch()
    const cc = new CompanyController(dispatch)

    const [city, setCity] = useState(company.city || '')
    const [country, setCountry] = useState(company.country || '')
    const [address, setAddress] = useState(company.address || '')
    const [category, setCategory] = useState(company.category || '')
    const [name, setName] = useState(company.name || '')
    const [username, setUsername] = useState(company.username || '')
    const [photo, setParentPhoto] = useState(new Blob())
    const companyIMG_URL = process.env.REQ_HOST + '/img/' + company.image_url

    const [citySuggestResult, setCitySuggestResult] = useState(null)
    const [citySuggest, setCitySuggest] = useState(false)

    const [countrySuggestResult, setCountrySuggestResult] = useState(null)
    const [countrySuggest, setCountrySuggest] = useState(false)

    const [categoryResult, setCategoryResult] = useState(null)
    const [categorySuggest, setCategorySuggest] = useState(false)

    const searchCountryListRequest = (country, city, resource) => {
        const url = process.env.REQ_HOST+'/fetch/'+ resource + 'list'

        axios.get(url, {params:{'page' : 0, 'country' : country, 'city' : city}})
        .then(_resp=> {
            if(_resp.data) {
                resource === 'city' && setCitySuggestResult(_resp.data)
                resource === 'country' && setCountrySuggestResult(_resp.data)
            }
        })
        .catch(_err => {
            console.log(_err)
            console.log(_err.response.data)
        })
    }

    const searchAnimeRequest = (name) => {
        const url = process.env.REQ_HOST + '/fetch/animelist'

        axios.get(url, {params: { 'page': 0, 'name': name } })
        .then(_resp => {
            setCategoryResult(_resp.data)
            console.log(_resp.data)
        })
        .catch(_err => {
            console.log(err)
            return false
        })
    }

    const handleInput = (e) => {
        let target = e.target
        let value = target.value || ''

        switch(target.name) {
            case 'name':
                setName(value)
                break
            case 'city':
                searchCountryListRequest(country, city, 'city');
                setCitySuggest(true);
                setCity(value)
                break
            case 'address':
                setAddress(value)
                break
            case 'country':
                searchCountryListRequest(country, city, 'country');
                setCountrySuggest(true);
                setCountry(value)
                break
            case 'category':
                searchAnimeRequest(value);
                setCategorySuggest(true);
                setCategory(value)
                break
            default:
                return
        }
    }

    const setValue = (target, val) => {
        switch(target) {
            case 'city':
                setCity(val)
                break
            case 'country':
                setCountry(val)
                break
            case 'category':
                setCategory(val)
                break
            default:
                return
        }
        setCitySuggest(false)
        setCountrySuggest(false)
        setCategorySuggest(false)
    }

    const openSuggest = (target) => {
        setCitySuggest(false)
        setCountrySuggest(false)
        setCategorySuggest(false)
        
        switch(target) {
            case 'city':
                setCitySuggest(true)
                break
            case 'country':
                setCountrySuggest(true)
                break
            case 'category':
                setCategorySuggest(true)
                break
            default:
                setCitySuggest(false)
                setCountrySuggest(false)
                setCategorySuggest(false)
                return
        }
    }

    const submitForm = (e) => {
        e.preventDefault()

        console.log(company)

        let carbonCompany = company;
        carbonCompany.name = name
        carbonCompany.city = city
        carbonCompany.country = country
        carbonCompany.address = address
        carbonCompany.category = category
        carbonCompany.username = username

        cc.updateDetails({
            company: carbonCompany,
            file: photo
        }, user.token)
        .then(r => {
            if(r.type === 'ok') {
                console.log(r.message)
            } else {
                console.log(r)
            }
        })
        .catch(e => {
            console.log(e)
            if(r.type === 'error') {
                console.log(e.message)
            }
        })
    }

    return (
        <form className={styles.inlineForm} onSubmit={e => submitForm(e)}>
            <div className={styles.formGroup}>
                <input name='name' value={name} type='text' placeholder="Company name" onChange={e => handleInput(e)}/>
            </div>

            <div className={styles.formGroup}>
                <input name='city' value={city} autoComplete="no" type='text' placeholder="Company city" onChange={e => handleInput(e)} onClick={e => openSuggest(e.target.name)}/>

                {
                    citySuggest &&
                    <div className={styles.autoSuggest}>
                        <ul>
                            {
                                Array.isArray(citySuggestResult) && citySuggestResult.map(c => {
                                    return(<li key={c.id} onClick={e => {setCitySuggest(true); setValue('city', c.city)}}>{c.city}</li>)
                                })
                            }     
                        </ul>
                    </div>
                }
            </div>

            <div className={styles.formGroup}>
                <input name='country' value={country} autoComplete="no" type='text' placeholder="Company country" onChange={e => handleInput(e)} onClick={e => openSuggest(e.target.name)}/>

                {
                    countrySuggest &&
                    <div className={styles.autoSuggest}>
                        <ul>
                            {
                                Array.isArray(countrySuggestResult) && countrySuggestResult.map(c => {
                                    return(<li key={c.id} onClick={e => {setCitySuggest(true); setValue('country', c.country)}}>{c.country}</li>)
                                })
                            }     
                        </ul>
                    </div>
                }
            </div>

            <div className={styles.formGroup}>
                <input name='address' value={address} type='text' placeholder="Company address" onChange={e => handleInput(e)}/>
            </div>

            <div className={styles.formGroup}>
                <input name='category' value={category} autoComplete="no" type='text' placeholder="Company category" onChange={e => handleInput(e)} onClick={e => openSuggest(e.target.name)}/>

                {
                    categorySuggest &&
                    <div className={styles.autoSuggest}>
                        <ul>
                            {
                                Array.isArray(categoryResult) && categoryResult.map(c => {
                                    return(<li key={c.id} onClick={e => {setCitySuggest(true); setValue('category', c.list)}}>{c.list}</li>)
                                })
                            }     
                        </ul>
                    </div>
                }
            </div>

            <div className={styles.formGroup}>
                <input name='username' value={username} type='text' placeholder="Company username" onChange={e => handleInput(e)}/>
            </div>

            <div className={styles.formGroup}>
                <FileUpload update={setParentPhoto} photo={companyIMG_URL}/>
            </div>

            <div className={styles.formGroup}>
                <input type='submit' value='Update'/>
            </div>
        </form>
    )
}

export default CompanyEdit