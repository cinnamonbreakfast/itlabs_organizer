import React, { useEffect, useState } from 'react'
import styles from '../styles/components/topNavigation.module.scss'
import { useDispatch, useSelector } from 'react-redux'
import { useRouter } from 'next/router'

import UserNav from './auth/userNav'

export const LINK_PROP = {
    DISPLAY_LOGGED: 'DISPLAY_LOGGED',
    DISPLAY_NOT_LOGGED: 'DISPLAY_NOT_LOGGED',
    DISPLAY_ALWAYS: 'DISPLAY_ALWAYS',
    IS_HOME: 'IS_HOME',
}

const TopNavigation = (props) => {
    const dispatcher = useDispatch()
    const user = useSelector(state => (state.user))

    return (
        <div className={styles.top_nav}>
            <div className={styles.logo}>
                <img src='/icon.svg'/>
                <h2>AppointmentApp</h2>
            </div>
            <div className={styles.center}>
                <div className={styles.links}>
                    <ul>
                        {
                            props.links.map((path, index) => {
                                    if(path.props.includes(LINK_PROP.DISPLAY_ALWAYS))
                                        return (<li key={index}><a className={path.type === 'button' ? 'button':null} href={path.url}>{path.name}</a></li>);
                                    else if(user.userLoggedIn && path.props.includes(LINK_PROP.DISPLAY_LOGGED)){
                                        return (<li key={index}><a className={path.type === 'button' ? 'button':null} href={path.url}>{path.name}</a></li>);
                                    }
                                    else if(!user.userLoggedIn && path.props.includes(LINK_PROP.DISPLAY_NOT_LOGGED))
                                        return (<li key={index}><a className={path.type === 'button' ? 'button':null} href={path.url}>{path.name}</a></li>);
                                }
                            )
                        }
                    </ul>
                </div>
            </div>
            <div className={styles.right}>
                <UserNav/>
            </div>
        </div>
    )
}


export default TopNavigation;