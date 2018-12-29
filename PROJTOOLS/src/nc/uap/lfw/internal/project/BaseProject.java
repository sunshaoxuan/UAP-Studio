/*
 * Created on 2005-8-10
 * @author ºÎ¹ÚÓî
 */
package nc.uap.lfw.internal.project;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.PlatformObject;

public abstract class BaseProject extends PlatformObject implements IProjectNature
{
	private IProject	fProject;

	public BaseProject()
	{
		super();
	}

	protected void addToBuildSpec(String builderID) throws CoreException
	{
		IProjectDescription description = getProject().getDescription();
		ICommand builderCommand = getBuilderCommand(description, builderID);
		if (builderCommand == null)
		{
			// Add a new build spec
			ICommand command = description.newCommand();
			command.setBuilderName(builderID);
			setBuilderCommand(description, command);
		}
	}

	private ICommand getBuilderCommand(IProjectDescription description, String builderId) throws CoreException
	{
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i)
		{
			if (commands[i].getBuilderName().equals(builderId))
			{
				return commands[i];
			}
		}
		return null;
	}

	public IProject getProject()
	{
		return fProject;
	}

	protected void removeFromBuildSpec(String builderID) throws CoreException
	{
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i)
		{
			if (commands[i].getBuilderName().equals(builderID))
			{
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i, commands.length - i - 1);
				description.setBuildSpec(newCommands);
				getProject().setDescription(description, null);
				return;
			}
		}
	}

	private void setBuilderCommand(IProjectDescription description, ICommand newCommand) throws CoreException
	{
		ICommand[] oldCommands = description.getBuildSpec();
		ICommand oldBuilderCommand = getBuilderCommand(description, newCommand.getBuilderName());
		ICommand[] newCommands;
		if (oldBuilderCommand == null)
		{
			// Add a build spec after other builders
			newCommands = new ICommand[oldCommands.length + 1];
			System.arraycopy(oldCommands, 0, newCommands, 0, oldCommands.length);
			newCommands[oldCommands.length] = newCommand;
		}
		else
		{
			for (int i = 0, max = oldCommands.length; i < max; i++)
			{
				if (oldCommands[i] == oldBuilderCommand)
				{
					oldCommands[i] = newCommand;
					break;
				}
			}
			newCommands = oldCommands;
		}
		// Commit the spec change into the project
		description.setBuildSpec(newCommands);
		getProject().setDescription(description, null);
	}

	public void setProject(IProject project)
	{
		this.fProject = project;
	}
}