import React from "react"
import { Link, useParams, useNavigate }from 'react-router-dom'
import api from '../api/axiosConfig.js'
import Task from '../components/Task.jsx'

export default function Category(props){
    const [category, setCategory] = React.useState(null)
    const [tasks, setTasks] = React.useState(null)
    const { id } = useParams()
    const [newTask, setNewTask] = React.useState(null)
    const [editingTask, setEditingTask] = React.useState(null)
    const token = localStorage.getItem('token')
    const navigate = useNavigate()

    React.useEffect(() => {
        const token = localStorage.getItem('token')
        async function getCategory(){
            try {
                const response = await api.get(`/categories/${id}`,
                    {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                })
                const data = response.data
                setCategory(data)
            } 
            catch (error) {
                console.error('Categories get error:', error)
            }
        }
        async function fetchTasks() {
            const tasks = await getTasks()
            setTasks(tasks)
        }
        getCategory()
        fetchTasks()
    }, [])

    async function getTasks(){
        try {
            const response = await api.get(`/categories/${id}/tasks`,
                {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            return response.data
        } 
        catch (error) {
            console.error('Tasks get error:', error)
            navigate("/login")
        }
    }

    function clearPastActions(){
        setNewTask(null)
        setEditingTask(null)
    }

    function toggleAddNewTask(){
        if(!newTask){
            clearPastActions()
            setNewTask({
                name: "",
                priority: "Low", 
                categoryId: id,
                adding: true
            })
        }
    }

    function handleNewTaskChange(event){
        const {name, value} = event.target
        setNewTask(prevTask => {
            return {
                ...prevTask,
                [name]: value
            }
        })
    }

    async function handleAddNewTask(){
        try {
            if(newTask.name.trim().length === 0)
                return
            const response = await api.post('/tasks', 
                {
                    name: newTask.name,
                    priority: newTask.priority, 
                    categoryId: newTask.categoryId,
                }, 
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            const tasks = await getTasks(); 
            setTasks(tasks); 
        } 
        catch (error) {
            console.error('Task add error:', error)
            navigate("/login")
        }
        finally {
            clearPastActions()
        }
    }

    function handleStartEditingTask(task, event){
        clearPastActions()
        const {idInString, name, priority} = task
        setEditingTask({
            name: name,
            priority: priority,
            id: idInString,
            complete: task.complete
        })
    }

    function handleEditingTaskChange(event){
        const {name, value} = event.target
        setEditingTask(prevTask => {
            return {
                ...prevTask,
                [name]: value
            }
        })
    }

    async function handleEditTask(){
        try {
            if(editingTask.name.trim().length === 0)
                return
            const response = await api.patch(`/tasks/${editingTask.id}`, 
                {
                    name: editingTask.name,
                    priority: editingTask.priority, 
                    complete: editingTask.complete
                }, 
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            const tasks = await getTasks(); 
            setTasks(tasks); 
        } 
        catch (error) {
            console.error('Tasks edit error:', error)
            navigate("/login")
        }
        finally {
            clearPastActions()
        }
    }

    async function handleCompleteTask(task){
        clearPastActions()
        try {
            const response = await api.patch(`/tasks/${task.idInString}`, 
                {
                    complete: !task.complete
                }, 
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            const tasks = await getTasks(); 
            setTasks(tasks); 
        } 
        catch (error) {
            console.error('Tasks complete error:', error)
            navigate("/login")
        }
    }

    async function handleDeleteTask(task){
        clearPastActions()
        try {
            const response = await api.delete(`/tasks/${task.idInString}`,  
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            const tasks = await getTasks(); 
            setTasks(tasks); 
        } 
        catch (error) {
            console.error('Task delete error:', error)
            navigate("/login")
        }
    }

    if(!category){
        return <div className="loading">Loading...</div>
    }

    return (
    <>
        {category && (
            <div className="category-container">
                <div className={`category-title-container ${category.color}`}>
                    <Link to="/home" className={`return-from-category-button`}>
                        <i className={`fa-solid fa-left-long ${category.color}`}></i>
                    </Link>
                    <h1 className="category-title-text">{category.name}</h1>
                </div>
                <div className="category-tasks-container">
                    {!category.static && (
                        newTask? (
                            <div className="task-container adding-task-container">
                                <div className="task-name-container">
                                    <label htmlFor="task-name">Name</label>
                                    <input 
                                        id="task-name"
                                        name="name"
                                        type="text" 
                                        placeholder="Homework"
                                        value={newTask.name}
                                        onChange={handleNewTaskChange}
                                        onKeyDown={(e) => e.key === 'Enter' && handleAddNewTask()}
                                        autoFocus
                                    />
                                </div>
                                <div className="task-priority-container">
                                    <label htmlFor="priority">Priority</label>
                                    <select value={newTask.priority} id="priority" name="priority" onChange={handleNewTaskChange}>
                                        <option value="LOW" label="Low"></option>
                                        <option value="MEDIUM" label="Med"></option>
                                        <option value="HIGH" label="High"></option>
                                    </select>
                                </div>
                                <div className="task-actions-container">
                                    <button onClick={clearPastActions}>cancel</button>
                                    <button onClick={handleAddNewTask}>create</button>
                                </div>
                            </div>
                        ) : (
                            <div onClick={toggleAddNewTask} className="task-container add-task-container">
                                <h2>Add Task</h2>
                            </div>
                        )
                    )}
                    {tasks?.map(task => {
                        return editingTask?.id == task.idInString?(
                            <div key={task.idInString} className="task-container adding-task-container">
                                <div className="task-name-container">
                                    <label htmlFor="task-name">Name</label>
                                    <input 
                                        id="task-name"
                                        name="name"
                                        type="text" 
                                        placeholder="Homework"
                                        value={editingTask.name}
                                        onChange={handleEditingTaskChange}
                                        onKeyDown={(e) => e.key === 'Enter' && handleEditTask()}
                                        autoFocus
                                    />
                                </div>
                                <div className="task-priority-container">
                                    <label htmlFor="priority">Priority</label>
                                    <select value={editingTask.priority} id="priority" name="priority" onChange={handleEditingTaskChange}>
                                        <option value="LOW" label="Low"></option>
                                        <option value="MEDIUM" label="Med"></option>
                                        <option value="HIGH" label="High"></option>
                                    </select>
                                </div>
                                <div className="task-actions-container">
                                    <button onClick={() => setEditingTask(null)}>cancel</button>
                                    <button onClick={handleEditTask}>update</button>
                                </div>
                            </div>
                        ):(
                            <Task 
                                key={task.idInString} 
                                task={task} 
                                handleStartEditingTask={handleStartEditingTask}
                                handleDeleteTask={handleDeleteTask}
                                handleCompleteTask={handleCompleteTask}
                            />
                        ) 
                    })}
                </div>
                
            </div>
        )}
    </>
    )
}

/**
 * id
 * idInString
 * userId
 * name
 * color
 * tasks
 * statics
 */