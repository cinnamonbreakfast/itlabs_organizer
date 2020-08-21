import { useState, useEffect } from 'react'
import styles from '../../styles/pages/companiesCreate.module.scss'
import { useSelector, useStore } from 'react-redux'
import axios from 'axios'
const FileUpload = (props) => {
    const [photo, setPhoto] = useState(null)
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
    const [mode, setMode] = useState(null)
    const [city, setCity] = useState('')
    const [address, setAddress] = useState('')
    const [country, setCountry] = useState('')
    const [category, setCategory] = useState('')
    const[companyName, setCompanyName] = useState('')
    const[searchResult,setSearchResult] = useState(null);
    const[searchCountryResult,setCountrySearchResult] = useState('');
    const[searchCityResult,setCityResult]= useState('')
    const[photo,setParentPhoto] = useState(new Blob())




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
    const searchAnimeRequest=(name)=>{
        const url = process.env.REQ_HOST+'/fetch/animelist'
        axios.get(url,{params:{'page':0,'name':name}})
        .then(_resp=>{
            {console.log(_resp);return setSearchResult(_resp.data)}
        }).catch(_err=>{console.log(_err) 
            return false})
    }

    const searchCountryListRequest= (country,city,query)=>{
        return new Promise(async (res,reject)=>{
        const url = process.env.REQ_HOST+query
        axios.get(url,{params:{'page':0,'country':country,'city':city}})
        .then(_resp=>{
            {res( _resp.data)}
        }).catch(_err=>{console.log(_err) 
            reject(false)})
        });
    }

    const setTextHandler = (event)=>{
        let child = event.target;
        let parent =child;
        
        while(parent.tagName.toString()!='LI')
        {
            parent = child.parentElement
            child=parent;
        }
        let firstChild = parent.firstElementChild
        
        if(firstChild.className=="category_p")
            setCategory(firstChild.textContent)
        else if(firstChild.className=="country_p"){
            setCountry(firstChild.textContent)
        }else {
            setCity(firstChild.textContent)
        }
      }
const handleSubmitButton= (e)=>{
    e.preventDefault()
    createCompanyRequest()
    
}
const createCompanyRequest = () =>{
    let formData = new FormData()
    formData.append('file',photo);
    formData.append('name',companyName);
    formData.append('city',city);
    formData.append('address',address);
    formData.append('category',category);
    formData.append('country',country);
    const url = process.env.REQ_HOST + '/c/create'
    axios.post(url,formData,{headers:{
        'Content-Type':'multipart/form-data',
        'token':user.token
    }}).then(e=>{
        console.log(e)
    }).catch(_err=>{
        console.log(_err)
    })
}
    return (
        <div className={styles.pageWrapper}>
            <div className={styles.title}>
                <h1>New company</h1>
                <p>Register a company within Appointment app.</p>
            </div>

            <form onSubmit={handleSubmitButton} className={styles.sides} autoComplete="fuckoffchrome">
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
                        <FileUpload update ={setParentPhoto} />
                    </div>

                    <div className={styles.formGroup}>
                        <label>City</label>
                        <input type="text" name="company_city" autoComplete="fuckoffchrome" value={city} 
                                onChange={(e) => {setCity(e.target.value)
                                searchCountryListRequest(country,e.target.value,'/fetch/citylist').then(e=>{setCityResult(e)})}}
                                onClick={(e)=>{
                                 searchCountryListRequest(country,e.target.value,'/fetch/citylist').then(e=>{setCityResult(e)})}}
                                 placeholder="Enter a city"/>

                                <div className={styles.autoSuggest + ' ' + styles.companies}>
                                    <ul>
                                        
                                            {Array.isArray(searchCityResult)&&searchCityResult.map(e=>{
                                                return(
                                                <li onClick={setTextHandler} >
                                                    <p className ="city_p">{e.city}</p>
                                                </li>
                                                )
                                        
                                            })
                                        }     
                                    </ul>
                                  </div>
                    </div>


                    <div className={styles.formGroup}>
                        <label>Address</label>
                        <input type="text" name="company_address"  autoComplete="fuckoffchrome" value={address} onChange={(e) => {setAddress(e.target.value); 
                        
                            }} placeholder="Enter an address"/>
                    </div>

                    <div className={styles.formGroup}>
                        <label>Country</label>
                        <input type="text" name="company_country" autoComplete="fuckoffchrome" value={country}
                         onChange={(e) => {setCountry(e.target.value);
                             searchCountryListRequest(e.target.value,city,'/fetch/countrylist').then(e=>{setCountrySearchResult(e)})}}
                        onClick={(e)=>{
                            searchCountryListRequest(e.target.value,city,'/fetch/countrylist').then(e=>{setCountrySearchResult(e)})}}   
                             placeholder="Enter a country"/>

                        <div className={styles.autoSuggest + ' ' + styles.companies}>
                            <ul>
                                
                                    {Array.isArray(searchCountryResult)&&searchCountryResult.map(e=>{
                                        return(
                                        <li onClick={setTextHandler} >
                                            <p className ="country_p">{e.country}</p>
                                        </li>
                                        )
                                
                                    })
                                }     
                            </ul>
                        </div>

                    </div>

                    <div className={styles.formGroup}>
                        <label>Category</label>
                        
                        <input id = "category_box" type="text" value={category} name="category" autoComplete="off" placeholder={category} onClick={(e) => { e.stopPropagation();searchAnimeRequest(e.target.value)  }} onChange={(e) => {setCategory(e.target.value);searchAnimeRequest(e.target.value)}}/>
                        
                        <div className={styles.autoSuggest + ' ' + styles.companies}>
                            <ul>
                                
                                    {Array.isArray(searchResult)&&searchResult.map(e=>{
                                        return(
                                        <li onClick={setTextHandler} >
                                            <p className="category_p">{e.list}</p>
                                        </li>
                                        )
                                
                                    })
                                }     
                            </ul>
                        </div>
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