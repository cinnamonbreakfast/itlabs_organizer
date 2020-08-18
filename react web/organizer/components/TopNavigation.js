import React, { useEffect, useState, useRouter } from 'react'
import styles from '../styles/components/topNavigation.module.scss'
import { useSelector } from 'react-redux'

export const LINK_PROP = {
    DISPLAY_LOGGED: 'DISPLAY_LOGGED',
    DISPLAY_NOT_LOGGED: 'DISPLAY_NOT_LOGGED',
    DISPLAY_ALWAYS: 'DISPLAY_ALWAYS',
    IS_HOME: 'IS_HOME',
}

const TopNavigation = (props) => {
    let user = useSelector(state => (state.user))

    console.log(user)

    return (
        <div className={styles.top_nav}>
            <div className={styles.logo}>AppointmentLab</div>
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
                
                {
                    user.userLoggedIn ?
                    <div className={styles.userTab}>
                        <div className={styles.userControl}>
                            <h2>Andrew C.</h2>
                        </div>

                        <div className={styles.userImg}>
                            {/* <img src=""/> */}

                            <div className={styles.noAvatar}>
                                A
                            </div>
                        </div>
                    </div>
                    :
                    <div className={styles.authTab}>
                        <a href='/signin'>Sign in</a>
                        <a className='button' href='/signup'>Sign Up</a>
                    </div>
                }
            </div>
        </div>
    )
}


export default TopNavigation;