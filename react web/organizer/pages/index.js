import React, { useState, useEffect } from 'react'
import styles from '../styles/pages/home.module.scss'
import { useRouter } from 'next/router'
import axios from 'axios'
import generator from '../utils/generator'

export default function Home() {
  const router = useRouter()

  const [cityAutoSuggestion, toggleCitySuggestions] = useState(false);
  const [companyAutoSuggestion, toggleCompanySuggestions] = useState(false);

  const[criteria, setCriteria] = useState(0)
  const[location, setLocation] = useState('')
  const[searchString, setSearchString] = useState('')
  
  const[searchResult, setSearchResult] = useState(null)

  const handleLocation = (event) => {
    event.target.innerText && setLocation(event.target.innerText)
  }

  const changeCriteria = (event) => {
    if(event.target.value=='company')
    {
      setCriteria(0);
    }else if(event.target.value=='service')
    {
      setCriteria(1);
    }
    else
    setCriteria(0);
  }

  const bgOutClick = (event) => {
    toggleCitySuggestions(false)
    toggleCompanySuggestions(false)
  }

  const callForSuggestions = () => {  
    let data = new FormData();
    console.log(criteria,location);
    data.set('type', criteria)
    data.set('search_input', searchString)
    data.set('second_box', location)

    axios.post(
      process.env.REQ_HOST+'/map/sugestion',
      data)
    .then(resp => {
     { return  setSearchResult(resp.data.reverse())}
    }).catch(err => {
      return false;
    })
  }

  const handleFirstBox = (event) => {
    setSearchString(event.target.value)

    if(!event.target.value)
    {
      setSearchResult(null)
    } else {
      callForSuggestions()
    }
  }

  const handleSecondBox = (event) => {
    setLocation(event.target.value)
    callForSuggestions()
    /*
    if(!event.target.value)
    {
      setSearchResult(null)
    } else {
      callForSuggestions()
    }*/
  }

  const submitToSearch = (event) => {
    event.preventDefault()

    router.push
  }
  const setTextHandler = (event)=>{
    let child = event.target;
    let parent =child;
    while(parent.tagName.toString()!='LI')
    {
        parent = child.parentElement
        child=parent;
        console.log(parent)
    }
  
    let d = document.getElementById(parent.id)
    let firstChild = d.firstElementChild
    setSearchString(firstChild.textContent)
    console.log(parent.childNodes)
  }

  return (
    <div className={styles.homeWrapper} onClick={(event) => bgOutClick(event)}>
      <div className={styles.header}>
        <h1>Welcome to PlanIt</h1>
        <p>Search your desired service and book a place. No more queues!</p>
      </div>

      <div className={styles.searchForm}>
        <form action="/search">
          <div className={styles.formGroup}>
            <select name="criteria" id="criteria" onChange={(e) => changeCriteria(e)}>
              <option value="company">Company</option>
              <option value="service">Service</option>
            </select>
            <input id = "search_box" type="text" value={searchString} name="search" autoComplete="off" placeholder={searchString} onClick={(e) => { e.stopPropagation(); bgOutClick();callForSuggestions(); toggleCompanySuggestions(true) }} onChange={(e) => handleFirstBox(e)}/>
            {
              Array.isArray(searchResult) && companyAutoSuggestion &&
              <div className={styles.autoSuggest + ' ' + styles.companies}>
                <ul>
                  
                  {
                  Array.isArray(searchResult) && searchResult.map((each) => { 
                    
                    return (<li id = {each.company.id} onClick={setTextHandler}>
                    <h2 >{each.company.name}</h2>
                    <p>{`${each.company.city}, ${each.company.country}`} &bull;
                    {
                      each.company.services.map(el=>{return el.name+' '})
                    }
                     </p>
                  </li>
                  )}
                  )}

                </ul>
              </div>
            }
          </div>

          <div className={styles.formGroup} >
            <input type="text" value={location} name="city" placeholder="City, country" autoComplete="off" onClick={(e) => { e.stopPropagation(); bgOutClick();callForSuggestions(); toggleCitySuggestions(true)}} onChange={(e) => handleSecondBox(e)}/>
            {
              Array.isArray(searchResult) && cityAutoSuggestion && 
              <div className={styles.autoSuggest}>
                <ul>
                  {
                    searchResult.map((each) => {
                      return (<li onClick={(e) => handleLocation(e)}>{`${each.company.city}, ${each.company.country}`}</li>)
                    })
                  }
                </ul>
              </div>
            }
          </div>

          <div className={styles.formGroup}>
            <input type="submit" value="Search"/>
          </div>
        </form>
      </div>

      <div className={styles.footer}>&copy; 2020 PlanIt</div>
    </div>
  )
}
