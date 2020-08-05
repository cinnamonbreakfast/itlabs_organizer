import React, { useState } from 'react'
import styles from '../styles/pages/home.module.scss'

export default function Home() {
  const [autoSuggestion, toggleSuggestions] = useState(false);

  return (
    <div className={styles.homeWrapper}>
      <div className={styles.header}>
        <h1>Welcome to PlanIt</h1>
        <p>Search your desired service and book a place. No more queues!</p>
      </div>

      <div className={styles.searchForm}>
        <form onSubmit={(e) => e.preventDefault()}>
          <div className={styles.formGroup}>
            <input type="text" name="service" placeholder="Name or service"/>
          </div>

          <div className={styles.formGroup}>
            <input type="text" name="city" placeholder="City" autoComplete="off" onBlur={() => toggleSuggestions(false)} onFocus={() => toggleSuggestions(true) }/>
            {
              autoSuggestion && 
              <div className={styles.autoSuggest}>
                <ul>
                  <li>Cluj-Napoca, RO</li>
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
