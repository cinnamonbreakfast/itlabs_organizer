import React, { useState, useEffect } from 'react'
import styles from '../styles/pages/home.module.scss'

export default function Home() {
  const [cityAutoSuggestion, toggleCitySuggestions] = useState(false);
  const [companyAutoSuggestion, toggleCompanySuggestions] = useState(false);

  const[criteria, setCriteria] = useState('company')
  const[location, setLocation] = useState('')
  const[searchString, setSearchString] = useState('')

  const handleLocation = (event) => {
    event.target.innerText && setLocation(event.target.innerText)
  }

  const bgOutClick = (event) => {
    toggleCitySuggestions(false)
    toggleCompanySuggestions(false)
  }

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
            <input autocomplete="off" type="text" name="service" placeholder={`${criteria.charAt(0).toUpperCase()+criteria.slice(1)} name`} onClick={(e) => { e.stopPropagation(); bgOutClick(); toggleCompanySuggestions(true) }}/>
            {
              companyAutoSuggestion &&
              <div className={styles.autoSuggest + ' ' + styles.companies}>
                <ul>
                  {/* TODO: click pe companie */}
                  <li>
                    <h2>Company name</h2>
                    <p>Cluj-Napoca, RO &bull; Tuns, Barbierit...</p>
                  </li>

                  <li>
                    <h2>Company name</h2>
                    <p>Location &bull; Tuns, Barbierit, Pensat ...</p>
                  </li>

                  <li>
                    <h2>Company name</h2>
                    <p>Location &bull; Tuns, Barbierit, Pensat ...</p>
                  </li>
                </ul>
              </div>
            }
          </div>

          <div className={styles.formGroup} >
            <input autocomplete="off" type="text" value={location} name="city" placeholder="City, country" autoComplete="off" onClick={(e) => { e.stopPropagation(); bgOutClick(); toggleCitySuggestions(true)}} onChange={(e) => setLocation(e.target.value)}/>
            {
              cityAutoSuggestion && 
              <div className={styles.autoSuggest}>
                <ul>
                  <li onClick={(e) => handleLocation(e)}>Cluj-Napoca, RO</li>
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
