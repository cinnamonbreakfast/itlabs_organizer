import React, { useEffect, useState, useRouter } from 'react'
import styles from '../styles/components/topNavigation.module.scss'
import { useSelector } from 'react-redux'

const TopNavigation = (props) => {
    let user = useSelector(state => (state.user))

    return (
        <div className={styles.top_nav}>
            <div className={styles.logo}>Org</div>
            <div className={styles.center}>{props.center}</div>
            <div className={styles.right}>
                <div className={styles.links}>
                    <ul>
                        {
                            props.links.map((path, index) => {
                                    if(path.always==true)
                                    return (<li key={index}><a className={path.type === 'button' ? 'button':null} href={path.url}>{path.name}</a></li>);
                                    else if(user.userLoggedIn==true && path.displayWhileLogged==true){
                                        return (<li key={index}><a className={path.type === 'button' ? 'button':null} href={path.url}>{path.name}</a></li>);
                                    }else
                                    if(path.displayWhileLogged==false && user.userLoggedIn==false ) return (<li key={index}><a className={path.type === 'button' ? 'button':null} href={path.url}>{path.name}</a></li>);
                                }
                            )
                        }
                    </ul>
                </div>
            </div>
        </div>
    )
}


export default TopNavigation;