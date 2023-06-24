import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weightwiseruicostafinalproject.MainActivity
import com.example.weightwiseruicostafinalproject.R

/**
 * Adapter class for the RecyclerView in MainActivity to display a list of weights.
 *
 * @param weights The array of weights to display.
 * @param listener The listener for item click events.
 */
class WeightListAdapter(
    private val weights: Array<Triple<String, String, String>>,
    private val listener: MainActivity
) : RecyclerView.Adapter<WeightListAdapter.ViewHolder>() {

    // Interface for item click listener
    interface OnItemClickListener {
        fun onItemClick(weight: Triple<String, String, String>)
    }

    /**
     * Creates and returns a ViewHolder object for the item view.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activit_grid_list_weights, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds the data to the ViewHolder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weight = weights[position]
        holder.textViewId.text = weight.first
        val prefixedWeight = "Weight: ${weight.second}"
        holder.textViewWeight.text = prefixedWeight
        holder.textViewDate.text = weight.third
    }

    /**
     * Returns the number of items in the list.
     */
    override fun getItemCount(): Int {
        return weights.size
    }

    /**
     * ViewHolder class for the item view.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textViewId: TextView = itemView.findViewById(R.id.textViewId)
        val textViewWeight: TextView = itemView.findViewById(R.id.textViewWeight)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)


        init {
            itemView.setOnClickListener(this)
        }

        /**
         * Handles the click event on the item view.
         */
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val weight = weights[position]
                listener.onItemClick(weight)
            }
        }
    }
}
