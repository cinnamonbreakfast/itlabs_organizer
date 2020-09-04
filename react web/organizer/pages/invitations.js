import { useSelector, useDispatch } from 'react-redux'
import { useEffect, useState } from 'react'
import UserController from './api/userController'

import styles from '../styles/pages/invitations.module.scss'
import { actions } from './api/redux/userActions'

const Invitation = (props) => {
    const invitation = props.invitation
    const photo_URL = invitation.companyDTO.image_url || null

    console.log(invitation)

    return (
        <li>
            <div className={styles.photo}>
                { photo_URL && <img src={process.env.REQ_HOST+'/img/' + photo_URL} />}
                <h1>{invitation.companyDTO.name[0]}</h1>
            </div>

            <h1>{invitation.companyDTO.name}</h1>
            <p>Job pisition: {invitation.serviceDTO.name}</p>

            <div className={styles.control}>
                <button onClick={e => props.accept(invitation.id)}>Accept</button>
                <button>Reject</button>
            </div>
        </li>
    )
}

const Invitations = () => {
    const user = useSelector(state => (state.user))
    const dispatch = useDispatch()
    const uc = new UserController(dispatch)
    const [invitations, setInvitations] = useState(null)

    console.log(user.token)

    useEffect(() => {
        dispatch({type: actions.SET_USER_INVITATIONS, payload: null})

        uc.getSpecialistData(user.token)
        .then(resp => {
            console.log(resp)
        })
    }, [])

    const acceptInvitation = (id) => {
        uc.acceptInvitation(id, user.token)
        .then(r => {
            console.log(r)
            uc.getSpecialistData(user.token)
        })
        .catch(e => {
            console.log(e)
        })
    }

    console.log(user.invitations)

    return (
        <div className={styles.pageWrapper}>
            <div className={styles.title}>
                <h1>Invitations</h1>
                <p>Here you can see your invitations to be an employee to a certain company. You can accept or reject those invitations. As an employee you will have access to the company schedule and you will have your own agenda inside the platform or people can reach to you.</p>
            </div>

            <ul className={styles.invitationsList}>
                {
                    Array.isArray(user.invitations) && user.invitations.map(inv => (
                        <Invitation key={inv.id} invitation={inv} accept={acceptInvitation}/>
                    ))
                }
            </ul>
        </div>
    )
}

export default Invitations