import { useDispatch } from 'react-redux'
import UserController from './api/userController'
import { useRouter } from 'next/router'

const Logout = () => {
    let dispatcher = useDispatch();
    let uc = new UserController(dispatcher)
    uc.logout()

    let router = useRouter();

    router.push('/')

    return (
        <div></div>
    )
}

export default Logout