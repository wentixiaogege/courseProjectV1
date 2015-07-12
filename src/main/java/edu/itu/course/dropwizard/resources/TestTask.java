package edu.itu.course.dropwizard.resources;

import io.dropwizard.servlets.tasks.Task;

import java.io.PrintWriter;
import java.sql.Time;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import edu.itu.course.dropwizard.api.beans.Device;


public class TestTask extends AbstractScheduledService {

	
	/* (non-Javadoc)
	 * @see com.google.common.util.concurrent.AbstractScheduledService#startUp()
	 */
	@Override
	protected void startUp() throws Exception {
		// TODO Auto-generated method stub
		super.startUp();
	}

	/* (non-Javadoc)
	 * @see com.google.common.util.concurrent.AbstractScheduledService#shutDown()
	 */
	@Override
	protected void shutDown() throws Exception {
		// TODO Auto-generated method stub
		super.shutDown();
	}

	/* (non-Javadoc)
	 * @see com.google.common.util.concurrent.AbstractScheduledService#executor()
	 */
	//using this one the webserver will die but automatically start the task

	private final Logger LOGGER = LoggerFactory.getLogger(TestTask.class);
	private final DeviceResourceImpl deviceResourceImpl;

	public TestTask(DeviceResourceImpl deviceResourceImpl) {
		this.deviceResourceImpl = deviceResourceImpl;
		// TODO Auto-generated constructor stub
	}

	int deviceId = 45;

	//using this one the server survives but need start the task
	/*@Override
	public void execute(ImmutableMultimap<String, String> arg0, PrintWriter arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("TestTask is" + new Date().toString());

		// using future to control????

		ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

		while (true) {
			Callable<String> asyncTask = new Callable<String>() {
				@Override
				public String call() throws Exception {

					Device sampleDevice = new Device(deviceId, "test Device", 0, "lightData");

					deviceId++;
					deviceResourceImpl.addDevice(sampleDevice);
					return "true";
				}
			};

			ListenableFuture<String> listenableFuture = executor.submit(asyncTask);

			Futures.addCallback(listenableFuture, new FutureCallback<String>() {
				public void onSuccess(String result) {
					// doMoreWithTheResultImmediately(result);
					System.out.println("Success");
				}

				public void onFailure(Throwable thrown) {
					// handleFailure(thrown);
					System.out.println("Falied");
				}
			});

			// doSomethingElse();

			try {
				String result = listenableFuture.get();
				// useResult(result);
			} catch (ExecutionException e) {
				// log.error("Task failed", e);
			} catch (InterruptedException e) {
				// log.warn("Task interrupted", e);
			}
			
			Thread.sleep(5000);
		}
		// executor.shutdown();

	}*/

	@Override
	protected void runOneIteration() throws Exception {

				Device sampleDevice = new Device(deviceId, "test Device", 0, "lightData");

				deviceId++;
				deviceResourceImpl.addDevice(sampleDevice);
				
				System.out.println("running");
	}

	@Override
	protected Scheduler scheduler() {
		// TODO Auto-generated method stub
		return AbstractScheduledService.Scheduler.newFixedRateSchedule(0, 5, TimeUnit.SECONDS);
	}

}
