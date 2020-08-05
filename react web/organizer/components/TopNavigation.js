import React, { useEffect, useState } from 'react'
import styles from '../styles/components/topNavigation.module.scss'

const TopNavigation = (props) => {

    return (
        <div className={styles.top_nav}>
            <div className={styles.logo}>Org</div>
            <div className={styles.center}>{props.center}</div>
            <div className={styles.right}>
                <div className={styles.links}>
                    <ul>
                        {
                            props.links.map((path, index) => (
                                    <li key={index}><a href={path.url}>{path.name}</a></li>
                                )
                            )
                        }
                    </ul>
                </div>
            </div>
        </div>
    )
}


export default TopNavigation;