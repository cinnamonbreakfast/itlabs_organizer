import styles from '../../styles/components/company/employee.module.scss'

const Employee = (props) => {
    const specialist = props.specialist
    const imgURL = ((specialist.user && specialist.user.imageURL) || specialist.imageURL);
    const name = ((specialist.user && specialist.user.name) || specialist.name);
    const phone = ((specialist.user && specialist.user.phone) || specialist.phone);
    
    return (
        <div className={styles.card}>
            { props.pending && <div className={styles.badge}>Pending</div> }
            { 1 && <div className={styles.owner}>👑</div> }

            <div className={styles.photo}>
                { imgURL && <img src={process.env.REQ_HOST+'/img/'+(imgURL)}/> }
                { !imgURL && <h1>{name[0]}</h1> }
            </div>

            <h2>{name}</h2>
            <p>{phone}</p>

            <div className={styles.hover}>
                { props.options && props.options.map(option => (
                    <button key={option.id} onClick={e => option.callBack(specialist)}>{option.text}</button>
                ))}
            </div>
        </div>
    )
}

export default Employee