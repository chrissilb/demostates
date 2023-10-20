package de.gwasch.code.demostates;

import de.gwasch.code.escframework.events.listeners.EventAdapter;
import de.gwasch.code.escframework.states.aggregations.AvgAggregation;
import de.gwasch.code.escframework.states.events.TransitionEvent;
import de.gwasch.code.escframework.states.states.AggregateState;
import de.gwasch.code.escframework.states.states.DivideState;
import de.gwasch.code.escframework.states.states.NumberCastState;
import de.gwasch.code.escframework.states.states.SimpleState;


public class App2 {
	private static class Boss {
		class EventHandler extends EventAdapter<TransitionEvent<Double>> {

			public void onFinish(TransitionEvent<Double> event, boolean success) {
				if (staffPerformance.getValue() < 0.9) {
					System.out.println("Staff performance went down to " + staffPerformance.getValue() + ".");
					fireSomeEmployees();
				}
			}
		}

		private AggregateState<Double> staffPerformanceBase;
		private DivideState<Double> staffPerformance;

		public Boss() {
			staffPerformanceBase = new AggregateState<>(Double.class, "staff performance base",
					new AvgAggregation<Double>(Double.class));
			SimpleState<Double> divisorState = new SimpleState<>(Double.class, "divisor", 10.0);
			staffPerformance = new DivideState<>(Double.class, "staff performance", staffPerformanceBase, divisorState);
			staffPerformance.registerTransitionListener(new EventHandler());
			
		}

		public void addEmployee(Employee e) {
			NumberCastState<Double, Integer> castState = new NumberCastState<>(Double.class, "cast", e.getPerformance());
			staffPerformanceBase.addChildState(castState);
		}

		public void fireSomeEmployees() {
			System.out.println("fire employees.");
		}
	}

	private static class Employee {
		private SimpleState<Integer> performance;

		public Employee(int performance) {
			this.performance = new SimpleState<>(Integer.class, "employee performance");
			this.performance.setValue(performance);
		}

		public SimpleState<Integer> getPerformance() {
			return performance;
		}

		public void setPerformanceValue(int performance) {
			this.performance.setValue(performance);
		}

		public void work() {
		}
	}
	
	public static void main(String[] args) {
		Boss boss = new Boss();
		Employee e = new Employee(10);
		boss.addEmployee(e);
		e = new Employee(10);
		boss.addEmployee(e);
		e = new Employee(10);
		boss.addEmployee(e);

		e.setPerformanceValue(5);
	}
}
