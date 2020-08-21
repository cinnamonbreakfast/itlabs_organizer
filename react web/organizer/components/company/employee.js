import styles from '../../styles/components/company/employee.module.scss'

const Employee = (props) => {

    return (
        <div className={styles.card}>
            <div className={styles.photo}>
                <img src="/avatars/u.jpg"/>
            </div>

            <h2>John D.</h2>
            <p>Barber</p>

            <a href="#">Select</a>
        </div>
    )
}

export default Employee