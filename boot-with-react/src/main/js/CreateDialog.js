import React from 'react';
import ReactDOM from 'react-dom';

class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        var newEmployee = {};
        this.props.attributes.forEach(attribute => {
            newEmployee[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(newEmployee);

        // Clear out the dialog's inputs.
        this.props.attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        var inputs = this.props.attributes.map(attribute => {
            return (
                <p key={ attribute }>
                  <input type="text" placeholder={ attribute } ref={ attribute } className="field" />
                </p>
            );
        });

        return (
            <div>
              <a href="#createEmployee">Create</a>
              <div id="createEmployee" className="modalDialog">
                <div>
                  <a href="#" title="Close" className="close">X</a>
                  <h2>Create new employee</h2>
                  <form>
                    { inputs }
                    <button onClick={ this.handleSubmit }>Create</button>
                  </form>
                </div>
              </div>
            </div>
        );
    }

}

CreateDialog.propTypes = {
    attributes: React.PropTypes.array,
    onCreate: React.PropTypes.func,
}

export default CreateDialog;
