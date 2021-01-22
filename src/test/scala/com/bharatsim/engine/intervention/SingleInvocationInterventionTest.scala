package com.bharatsim.engine.intervention

import com.bharatsim.engine.cache.PerTickCache
import com.bharatsim.engine.execution.Simulation
import com.bharatsim.engine.execution.control.{BehaviourControl, StateControl}
import com.bharatsim.engine.graph.GraphProvider
import com.bharatsim.engine.{Context, Dynamics, SimulationConfig}
import org.mockito.MockitoSugar.mock
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SingleInvocationInterventionTest extends AnyWordSpec with Matchers {
  "SingleInvocationInterventionTest" should {
    "be invoked only once even when start condition is satisfied multiple times" in {
      var oneTimeCount = 0
      val shouldActivate = (context: Context) => context.getCurrentStep % 2 == 0
      val shouldDeactivate = (context: Context) => context.getCurrentStep % 2 == 1
      val oneTimeAction = (_: Context) => oneTimeCount += 1
      val intervention: Intervention = SingleInvocationIntervention("testIntervention", shouldActivate, shouldDeactivate, oneTimeAction)

      val context = new Context(mock[GraphProvider], mock[Dynamics], SimulationConfig(10), mock[PerTickCache])
      val simulation = new Simulation(context, new BehaviourControl(context), new StateControl(context))

      context.interventions.add(intervention)
      simulation.run()

      oneTimeCount shouldBe 1
    }
  }
}
