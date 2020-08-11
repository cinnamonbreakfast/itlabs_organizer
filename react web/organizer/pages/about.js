import { useSelector } from 'react-redux'

const About = () => {
    let user = useSelector(state => {return state.user})

    console.log(user.userLoggedIn)

    return (
        <div style={{padding: '100px'}}>
            <p>dasd</p>
            <h1>{`User logged: ${user.userLoggedIn}`}</h1>
            {/* <h1>{`User data: ${user.data.name}`}</h1> */}
        </div>
    )
}

export default About