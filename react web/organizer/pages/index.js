import React, { useState, useEffect } from 'react'
import styles from '../styles/pages/home.module.scss'

import axios from 'axios'

export default function Home() {
  const [cityAutoSuggestion, toggleCitySuggestions] = useState(false);
  const [companyAutoSuggestion, toggleCompanySuggestions] = useState(false);

  const[criteria, setCriteria] = useState('company')
  const[location, setLocation] = useState('')
  const[searchString, setSearchString] = useState('')
  
  const[searchResult, setSearchResult] = useState(null)

  const handleLocation = (event) => {
    event.target.innerText && setLocation(event.target.innerText)
  }

  const bgOutClick = (event) => {
    toggleCitySuggestions(false)
    toggleCompanySuggestions(false)
  }

  const handleFirstBox = (event) => {
    setSearchString(event.target.value)


    if(!event.target.value)
    {
      setSearchResult(null)
    } else {
      let data = new FormData();
      data.set('type', 0)
      data.set('search_input', searchString)
      data.set('second_box', '')

      axios.post(
        'http://31.5.22.129:8080/map/sugestion',
        data)
        .then(resp => {
          console.log(resp)
          setSearchResult(resp.data)
        }).catch(err => {
          console.log(err)
          return false;
        })
    }
  }

  const handleSecondBox = (event) => {
    setLocation(event.target.value)

    
    if(!event.target.value)
    {
      setSearchResult(null)
    } else {
      let data = new FormData();
      data.set('type', 0)
      data.set('search_input', searchString)
      data.set('second_box', '')

      axios.post(
        'http://31.5.22.129:8080/map/sugestion',
        data)
        .then(resp => {
          setSearchResult(resp.data)
        }).catch(err => {
          console.log(err)
          return false;
        })
    }
  }

  console.log(searchResult)

  return (
    <div className={styles.homeWrapper} onClick={(event) => bgOutClick(event)}>
      <div className={styles.header}>
        <h1>Welcome to PlanIt</h1>
        <p>Search your desired service and book a place. No more queues!</p>
      </div>

      <div className={styles.searchForm}>
        <form onSubmit={(e) => e.preventDefault()}>
          <div className={styles.formGroup}>
            <select id="cars" onChange={(e) => setCriteria(event.target.value)}>
              <option value="company">Company</option>
              <option value="service">Service</option>
            </select>
            <input type="text" value={searchString} name="service" autoComplete="off" placeholder={`${criteria.charAt(0).toUpperCase()+criteria.slice(1)} name`} onClick={(e) => { e.stopPropagation(); bgOutClick(); toggleCompanySuggestions(true) }} onChange={(e) => handleFirstBox(e)}/>
            {
              Array.isArray(searchResult) && companyAutoSuggestion &&
              <div className={styles.autoSuggest + ' ' + styles.companies}>
                <ul>
                  
                  {
                  Array.isArray(searchResult) && searchResult.reverse().map((each) => { return (<li>
                    <h2>{each.company.name}</h2>
                    <p>{`${each.company.city}, ${each.company.country}`} &bull; Tuns, Barbierit...</p>
                  </li>
                  )}
                  )}

                </ul>
              </div>
            }
          </div>

          <div className={styles.formGroup} >
            <input type="text" value={location} name="city" placeholder="City, country" autoComplete="off" onClick={(e) => { e.stopPropagation(); bgOutClick(); toggleCitySuggestions(true)}} onChange={(e) => handleSecondBox(e)}/>
            {
              Array.isArray(searchResult) && cityAutoSuggestion && 
              <div className={styles.autoSuggest}>
                <ul>
                  {
                    searchResult.reverse().map((each) => {
                      return (<li onClick={(e) => handleLocation(e)}>{`${each.company.city}, ${each.company.country}`}</li>)
                    })
                  }
                </ul>
              </div>
            }
          </div>

          <div className={styles.formGroup}>
            <input type="submit" name="submit" value="Search"/>
          </div>
        </form>
      </div>

      <div className={styles.footer}>&copy; 2020 PlanIt</div>
    </div>
  )
}
