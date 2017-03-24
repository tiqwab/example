import React from 'react';

class Employee extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        console.log(this.props.employee);
        this.props.onDelete(this.props.employee);
    }

    render() {
        return (
            <tr>
              <td>{ this.props.employee.firstName }</td>
              <td>{ this.props.employee.lastName }</td>
              <td>{ this.props.employee.description }</td>
              <td>
                <button onClick={ this.handleDelete }>Delete</button>
              </td>
            </tr>
        );
    }

}

Employee.propTypes = {
    onDelete: React.PropTypes.func,
    employee: React.PropTypes.object,
}

export default Employee;
