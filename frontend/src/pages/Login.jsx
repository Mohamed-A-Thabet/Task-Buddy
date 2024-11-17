import React from 'react'
import { useNavigate } from 'react-router-dom'
import LoginForm from '../components/LoginForm'
import SignupForm from '../components/SignupForm'
import '../css/login.css'
import api from '../api/axiosConfig.js'


export default function Login(){
    const [isLogin, setIsLogin] = React.useState(true)
    const [signupData, setSignupData] = React.useState({
        username: "",
        email: "",
        password: "",
        confirmPassword: ""
    })
    const [loginData, setLoginData] = React.useState({
        usernameEmail: "",
        password: "",
    })
    const [submitStatus, setSubmitStatus] = React.useState({
        login: false,
        signup: false
    })
    const [error, setError] = React.useState(null)
    const navigate = useNavigate()
    localStorage.removeItem('token')

    function switchForm(event){
        if(event.target.id === "select-login" && isLogin) return
        if(event.target.id === "select-signup" && !isLogin) return
        document.getElementById("select-login").classList.toggle("bottom-border")
        document.getElementById("select-signup").classList.toggle("bottom-border")
        setIsLogin(prevIsLogin => !prevIsLogin)
        setError(null)
    }

    function handleSignupChange(event){
        const {name, value} = event.target
        setSignupData(prevFormData => {
            return {
                ...prevFormData,
                [name]: value
            }
        })
    }

    async function handleSignupSubmit(event){
        event.preventDefault()
        setSubmitStatus(prevStatus => {
            return {
                ...prevStatus,
                signup: true
            }
        })

        try {
            if(signupData.password !== signupData.confirmPassword){
                throw new Error("Passwords have to match!")
            }
            const response = await api.post('/auth/register', {
                username: signupData.username,
                email: signupData.email,
                password: signupData.password,
            });
            if(response.data.user == null){
                throw (response.data.message)
            }
            localStorage.setItem('token', response.data.jwt)
            navigate("/home")
        } 
        catch (error) {
            setError(error)
        }
        finally {
            setSubmitStatus({
                login: false,
                signup: false
            })
        }
    }

    function handleLoginChange(event){
        const {name, value} = event.target
        setLoginData(prevFormData => {
            return {
                ...prevFormData,
                [name]: value
            }
        })
    }

    async function handleLoginSubmit(event){
        event.preventDefault()

        setSubmitStatus(prevStatus => {
            return {
                ...prevStatus,
                login: true
            }
        })

        try {
            const response = await api.post('/auth/login', {
                username: loginData.usernameEmail,
                password: loginData.password,
            });
            if(response.data.user == null){
                throw (response.data.message)
            }
            localStorage.setItem('token', response.data.jwt)
            navigate("/home")
        } 
        catch (error) {
            setError(error)
        }
        finally {
            setSubmitStatus({
                login: false,
                signup: false
            })
        }
    }

    return (
    <>
        <div className="auth-container">
            <div className="form-type">
                <button id="select-login" className="bottom-border" onClick={switchForm}>Login</button>
                <button id="select-signup" onClick={switchForm}>Signup</button>
            </div>
            {error && (
                <h3 className='error-message'>{error}</h3>
            )}
            <form className="form">
                {isLogin? 
                    <LoginForm loginData={loginData} submitStatus={submitStatus} handleChange={handleLoginChange} submitForm={handleLoginSubmit}/> : 
                    <SignupForm signupData={signupData} submitStatus={submitStatus} handleChange={handleSignupChange} submitForm={handleSignupSubmit}/>
                }
            </form>
        </div>
    </>
    )
}

/**
 * stack IDK (gives the path of error but cant find the source)
 * message NO (Network Error)
 * name NO (Axios Error)
 * code NO (ERR_NETWORK)
 * config NO (none of the attributes give useful info
 * request NO (empty
 * Object.getOwnPropertyNames()
 */