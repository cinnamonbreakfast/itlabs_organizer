import { useState } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { useRouter } from 'next/router'
import styles from '../../styles/components/topNavigation.module.scss'

import { actions } from '../../pages/api/redux/userActions'

const UserNav = () => {
    const dispatcher = useDispatch()
    const user = useSelector(state => (state.user))
    const router = useRouter()

    const [userMenu, switchUserMenu] = useState(false)

    const navbarLogout = (e) => {
        e.preventDefault()

        dispatcher({type: actions.LOGOUT})

        router.push('/')
    }

    if(user.userLoggedIn && user.data)
    return (
        <div className={styles.userTab}>
            <div className={styles.userControl} onClick={() => switchUserMenu(!userMenu)}>
                <svg className={ userMenu ? styles.active : null } xmlns="http://www.w3.org/2000/svg" width="12" height="7" viewBox="0 0 12 7"><g transform="translate(1 1)"><path d="M10,0,5,5l.781-.781ZM0,0,5,5Z"/><path d="M 0 0 L 5.000400066375732 5.000400066375732 L 0 0 M 9.999899864196777 0 L 5.000400066375732 5.000400066375732 L 5.781569957733154 4.219089984893799 L 9.999899864196777 0 M 0 -1 C 0.2559223175048828 -1 0.5118446350097656 -0.9023699760437012 0.7071104049682617 -0.7071099281311035 L 5.000341415405273 3.586121559143066 L 9.292730331420898 -0.7070398330688477 C 9.487975120544434 -0.9023199081420898 9.743980407714844 -0.9999985694885254 9.99990177154541 -0.9999985694885254 C 10.25582313537598 -0.9999985694885254 10.51165962219238 -0.9024147987365723 10.70693969726563 -0.7071700096130371 C 11.09749984741211 -0.3166799545288086 11.09755992889404 0.3164801597595215 10.70707035064697 0.7070398330688477 L 6.488739967346191 4.926129817962646 L 5.710114479064941 5.70489501953125 C 5.709236621856689 5.705779552459717 5.708391666412354 5.70662784576416 5.707509994506836 5.707509994506836 C 5.512244701385498 5.902770042419434 5.256322383880615 6.000400066375732 5.000400066375732 6.000400066375732 C 4.974657535552979 6.000400066375732 4.948886394500732 5.999409198760986 4.923206329345703 5.997433662414551 C 4.752387046813965 5.98430871963501 4.584436893463135 5.927507400512695 4.437240600585938 5.826970100402832 C 4.436622619628906 5.82654857635498 4.435996532440186 5.826118469238281 4.435379505157471 5.825695037841797 C 4.435164928436279 5.825547695159912 4.434918880462646 5.825379848480225 4.434704303741455 5.82523250579834 C 4.384913444519043 5.791003704071045 4.33755350112915 5.751772403717041 4.293290138244629 5.707509994506836 L -0.7071104049682617 0.7071099281311035 C -1.097630500793457 0.3165798187255859 -1.097630500793457 -0.3165798187255859 -0.7071104049682617 -0.7071099281311035 C -0.5118446350097656 -0.9023699760437012 -0.2559223175048828 -1 0 -1 Z"/></g></svg>
                <h2>{user.data.name}</h2>
            </div>

            <div className={styles.userDropMenu + ' ' + (userMenu ? styles.active : null)}>
                <ul>
                    <li><a href='/u'>Profile</a></li>
                    <li><a href='/companies'>My Companies</a></li>
                    <li><a href='/appointments'>My Appointments</a></li>
                    <li><a href='/settings'>Settings</a></li>
                    <li><a href='/logout' onClick={(e) => navbarLogout(e)}>Sign Out</a></li>
                </ul>
            </div>

            <div className={styles.userImg}>
                {/* <img src=""/> */}

                <div className={styles.noAvatar}>
                    {user.data.name[0]}
                </div>
            </div>
        </div>
    )

    return (
        <div className={styles.authTab}>
            <a href='/signin'>Sign in</a>
            <a className='button' href='/signup'>Sign Up</a>
        </div>
    )
}

export default UserNav