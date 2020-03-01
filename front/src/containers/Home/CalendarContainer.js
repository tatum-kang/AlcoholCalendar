import React from 'react';

import { connect } from 'react-redux';
import { changeDate } from 'redux/modules/calendar';
import { bindActionCreators } from 'redux';
import { Calendar } from 'components/Home';


class CalendarContainer extends React.PureComponent{

  componentDidMount() {
    //console.log(typeof this.props.changeDate)
    //this.props.changeDate(moment().add(1, 'month'))
  }
  render() {
    console.log(this.props);
    const { props } = this;
    return (
      <Calendar date={props.date} changeDate={props.changeDate} />
    )
  }
}


export default connect(
  ({ calendar }) => ({
    date: calendar.date
  }),
  (dispatch) => ({
    changeDate: bindActionCreators(changeDate, dispatch)
  })
)(CalendarContainer);