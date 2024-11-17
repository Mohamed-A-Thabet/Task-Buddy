import React from 'react'

export default function Task(props){
    const {name, priority, complete} = props.task

    const priorityColor = `task-priority-${priority.toLowerCase()}`
    
    return (
        <div className={`task-container ${complete? "completed-task" : "white"}`}>
            <div className='task-actions'>
                <i className="fa-regular fa-pen-to-square" onClick={() => props.handleStartEditingTask(props.task)}></i>
                <i className="fa-solid fa-trash" onClick={() => props.handleDeleteTask(props.task)}></i>
                <i className="fa-regular fa-circle-check" onClick={() => props.handleCompleteTask(props.task)}></i>
            </div>
            <div className='task-title'>
                <p>{name}</p>
            </div>
            {!complete && (
                <div className={`task-footer ${priorityColor}`}></div>
            )}
        </div>
    )
}