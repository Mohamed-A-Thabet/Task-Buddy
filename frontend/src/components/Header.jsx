import '../css/header.css'
import { Link } from 'react-router-dom'

export default function Header(){
    const loggedIn = localStorage.getItem('token')

    return (
    <>
        <div className={`header-container ${loggedIn? "post-auth" : ""}`}>
            <Link
                to={`/home`}
            >
                <h1>Task Buddy</h1>
            </Link>
            {loggedIn && (
                <Link
                    to={`/setting`}
                >
                    <i className="fa-solid fa-user"></i>
                </Link>
            )}
        </div>
    </>
    )
}