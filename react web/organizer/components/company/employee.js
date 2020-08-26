import styles from '../../styles/components/company/employee.module.scss'

const Employee = (props) => {
    const specialist = props.specialist

    return (
        <div className={styles.card}>
            { 1 && <div className={styles.badge}>Pending</div> }

            <div className={styles.photo}>
                { specialist.user.imageURL && <img src={process.env.REQ_HOST+'/img/'+specialist.user.imageURL}/> }
                { !specialist.user.imageURL && <h1>{specialist.user.name[0]}</h1> }
            </div>

            <h2>{specialist.user.name}</h2>
            <p>{specialist.user.phone}</p>

            <div className={styles.hover}>
                { props.options && props.options.map(option => (
                    <button key={option.id} onClick={e => option.callBack(specialist)}>{option.text}</button>
                ))}
            </div>
        </div>
    )
}

export default Employee