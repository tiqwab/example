import React from 'react';
import ReactDOM from 'react-dom';

class UpdateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        var updatedEmployee = {};
        this.props.attributes.forEach(attribute => {
            updatedEmployee[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        updatedEmployee.manager = this.props.employee.manager;
        this.props.onUpdate(this.props.employee, updatedEmployee);
        window.location = "#";
    }

    render() {
        const inputs = this.props.attributes.map(attribute => (
            <p key={ this.props.employee[attribute] }>
              <input type="text"
                     placeholder={ attribute }
                     defaultValue={ this.props.employee[attribute] }
                     ref={ attribute }
                     className="field" />
            </p>
        ));

        const dialogId = "updateEmployee-" + this.props.employee._links.self.href;

        return (
            <div key={ this.props.employee._links.self.href }>
              <a href={ "#" + dialogId }>Update</a>
              <div id={ dialogId } className="modalDialog">
                <div>
                  <a href="#" title="close" className="close">X</a>
                  <h2>Update an employee</h2>
                  <form>
                    { inputs }
                    <button onClick={ this.handleSubmit }>Update</button>
                  </form>
                </div>
              </div>
            </div>
        );
    }

}

UpdateDialog.propTypes = {
    attributes: React.PropTypes.array,
    employee: React.PropTypes.object,
    onUpdate: React.PropTypes.func,
};

export default UpdateDialog;
