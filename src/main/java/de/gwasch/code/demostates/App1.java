package de.gwasch.code.demostates;

import de.gwasch.code.escframework.events.listeners.EventAdapter;
import de.gwasch.code.escframework.states.aggregations.AggregateFunction;
import de.gwasch.code.escframework.states.aggregations.DoubleAggregation;
import de.gwasch.code.escframework.states.events.TransitionEvent;
import de.gwasch.code.escframework.states.states.AggregateState;
import de.gwasch.code.escframework.states.states.SimpleState;



public class App1 {
	private static class Boss {
		class EventHandler extends EventAdapter<TransitionEvent<Double>> {

			public void onFinish(TransitionEvent<Double> event, boolean success) {
				if (staffPerformance.getValue() < 0.98) {
					System.out.println("Staff performance went down to " + staffPerformance.getValue() + ".");
					fireSomeEmployees();
				}
			}
		}

		private AggregateState<Double> staffPerformance;

		public Boss() {
			staffPerformance = new AggregateState<>(Double.class, "staff performance",
					new DoubleAggregation(AggregateFunction.Avg));
			staffPerformance.registerTransitionListener(new EventHandler());
		}

		public void addEmployee(Employee e) {
			staffPerformance.addChildState(e.getPerformance());
		}

		public void fireSomeEmployees() {
			System.out.println("fire employees.");
		}
	}

	private static class Employee {
		private SimpleState<Double> performance;

		public Employee(double performance) {
			this.performance = new SimpleState<>(Double.class, "employee performance");
			this.performance.setValue(performance);
		}

		public SimpleState<Double> getPerformance() {
			return performance;
		}

		public void setPerformanceValue(double performance) {
			this.performance.setValue(performance);
		}

		public void work() {
		}
	}
	
	public static void main(String[] args) {
		Boss boss = new Boss();
		Employee e = new Employee(1.0);
		boss.addEmployee(e);
		e = new Employee(1.0);
		boss.addEmployee(e);
		e = new Employee(1.0);
		boss.addEmployee(e);

		e.setPerformanceValue(0.5);
	}
}
